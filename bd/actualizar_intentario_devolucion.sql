DELIMITER ;;
CREATE TRIGGER `actualizar_inventario_devolucion` AFTER INSERT ON `movimientos` 
FOR EACH ROW 
BEGIN
    DECLARE equivalencia DECIMAL(10,2) DEFAULT 1;
    DECLARE codigo_principal VARCHAR(30);
    DECLARE existencia_actual DECIMAL(10,2) DEFAULT 0;
    DECLARE unidad_producto VARCHAR(30);
    
    -- Solo procesar si el movimiento es una devolución
    IF NEW.Movimiento = 'Devolucion' THEN
        -- Primero intentar obtener la unidad del producto
        SELECT p.Unidad INTO unidad_producto
        FROM producto p
        WHERE p.ClaveAlterna = NEW.Codigo
        LIMIT 1;
        
        -- Si no se encuentra en producto, buscar en subproducto
        IF unidad_producto IS NULL THEN
            SELECT p.Unidad INTO unidad_producto
            FROM subproducto s
            JOIN producto p ON s.Codigo = p.Codigo
            WHERE s.ClaveAlterna = NEW.Codigo
            LIMIT 1;
        END IF;
        
        -- Obtener el formato (equivalencia) de la unidad
        IF unidad_producto IS NOT NULL THEN
            SELECT COALESCE(Formato, 1) INTO equivalencia
            FROM unidad 
            WHERE Unidad = unidad_producto;
        END IF;
        
        -- Primero intentar encontrar el producto directamente
        SELECT Codigo, Existencia INTO codigo_principal, existencia_actual
        FROM producto
        WHERE ClaveAlterna = NEW.Codigo
        LIMIT 1;
        
        -- Si no se encuentra, buscar en subproductos
        IF codigo_principal IS NULL THEN
            SELECT p.Codigo, p.Existencia INTO codigo_principal, existencia_actual
            FROM subproducto s
            JOIN producto p ON s.Codigo = p.Codigo
            WHERE s.ClaveAlterna = NEW.Codigo
            LIMIT 1;
        END IF;
        
        -- Actualizar la existencia del producto principal si se encontró
        IF codigo_principal IS NOT NULL AND existencia_actual IS NOT NULL THEN
            UPDATE producto
            SET Existencia = existencia_actual + (NEW.Cantidad * equivalencia)
            WHERE Codigo = codigo_principal;
            
            -- Verificar si existe la tabla log_operaciones antes de insertar
            IF EXISTS (SELECT 1 FROM information_schema.tables WHERE table_schema = DATABASE() AND table_name = 'log_operaciones') THEN
                -- Registrar en el log para depuración
                INSERT INTO log_operaciones (operacion, descripcion, fecha)
                VALUES ('Devolucion', 
                        CONCAT('Producto: ', NEW.Codigo, 
                               ', Cantidad: ', NEW.Cantidad, 
                               ', Formato: ', equivalencia, 
                               ', Inventario anterior: ', existencia_actual, 
                               ', Nuevo inventario: ', existencia_actual + (NEW.Cantidad * equivalencia)),
                        NOW());
            END IF;
        END IF;
    END IF;
END;;
DELIMITER ;