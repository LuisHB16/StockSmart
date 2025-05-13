-- Trigger para devolver productos al inventario cuando se cancela un pedido
DELIMITER //
CREATE TRIGGER devolver_inventario_cancelacion BEFORE UPDATE ON venta
FOR EACH ROW
BEGIN
    DECLARE done INT DEFAULT FALSE;
    DECLARE codigo_producto VARCHAR(100);
    DECLARE cantidad_producto DOUBLE;
    DECLARE unidad_producto VARCHAR(30);
    DECLARE equivalencia INT;
    DECLARE producto_codigo VARCHAR(30);
    
    -- Cursor para obtener los detalles de la venta/pedido
    DECLARE cur CURSOR FOR 
        SELECT Codigo, Cantidad, Unidad 
        FROM detalle_venta 
        WHERE Id_Ventas = OLD.Id_Ventas;
    
    DECLARE CONTINUE HANDLER FOR NOT FOUND SET done = TRUE;
    
    -- Si el estado cambia de "Pedido" a "Cancelado", devolver los productos al inventario
    IF (OLD.Status = 'Pedido' OR OLD.Status = 'enviado') AND NEW.Status = 'Cancelado' THEN
        
        OPEN cur;
        
        read_loop: LOOP
            FETCH cur INTO codigo_producto, cantidad_producto, unidad_producto;
            
            IF done THEN
                LEAVE read_loop;
            END IF;
            
            -- Obtener la equivalencia de la unidad
            SELECT Formato INTO equivalencia
            FROM unidad
            WHERE Unidad = unidad_producto;
            
            -- Intentar encontrar el producto directamente
            SELECT Codigo INTO producto_codigo
            FROM producto
            WHERE ClaveAlterna = codigo_producto;
            
            IF producto_codigo IS NULL THEN
                -- Si no se encontró en producto, buscar en subproductos
                SELECT Codigo INTO producto_codigo
                FROM subproducto
                WHERE ClaveAlterna = codigo_producto;
                
                IF producto_codigo IS NOT NULL THEN
                    -- Devolver la cantidad al inventario
                    UPDATE producto
                    SET Existencia = Existencia + (cantidad_producto * equivalencia)
                    WHERE Codigo = producto_codigo;
                END IF;
            ELSE
                -- Devolver la cantidad al inventario
                UPDATE producto
                SET Existencia = Existencia + (cantidad_producto * equivalencia)
                WHERE Codigo = producto_codigo;
            END IF;
            
        END LOOP;
        
        CLOSE cur;
        
        -- Registrar el movimiento en la tabla de movimientos
        INSERT INTO movimientos(Movimiento, Fecha, Cantidad, Codigo, inventario_antes)
        SELECT 'Cancelacion', CURDATE(), dv.Cantidad, dv.Codigo, p.Existencia
        FROM detalle_venta dv
        JOIN producto p ON p.ClaveAlterna = dv.Codigo
        WHERE dv.Id_Ventas = OLD.Id_Ventas;
        
    END IF;
END //
DELIMITER ;