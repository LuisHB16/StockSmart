-- Trigger para registrar movimientos de inventario cuando se realizan ventas o pedidos
DELIMITER //
CREATE TRIGGER registrar_movimiento_inventario AFTER INSERT ON detalle_venta
FOR EACH ROW
BEGIN
    DECLARE estatus_venta VARCHAR(30);
    DECLARE inventario_actual DOUBLE;
    DECLARE producto_codigo VARCHAR(30);
    
    -- Obtener el estatus de la venta/pedido
    SELECT Status INTO estatus_venta
    FROM venta
    WHERE Id_Ventas = NEW.Id_Ventas;
    
    -- Obtener el código del producto y su existencia actual (inventario antes de la operación)
    SELECT Codigo, Existencia INTO producto_codigo, inventario_actual
    FROM producto
    WHERE ClaveAlterna = NEW.Codigo;
    
    IF producto_codigo IS NULL THEN
        -- Si no se encontró en producto, buscar en subproductos
        SELECT p.Codigo, p.Existencia INTO producto_codigo, inventario_actual
        FROM subproducto s
        JOIN producto p ON s.Codigo = p.Codigo
        WHERE s.ClaveAlterna = NEW.Codigo;
    END IF;
    
    -- Registrar el movimiento según el tipo de operación
    -- Guardamos el inventario_actual como inventario_antes (valor antes de la operación)
    IF estatus_venta = 'Venta' THEN
        INSERT INTO movimientos(Movimiento, Fecha, Cantidad, Codigo, inventario_antes)
        VALUES ('Venta', DATE_FORMAT(CURDATE(), '%d/%m/%Y'), NEW.Cantidad, NEW.Codigo, inventario_actual);
    ELSEIF estatus_venta = 'Pedido' THEN
        INSERT INTO movimientos(Movimiento, Fecha, Cantidad, Codigo, inventario_antes)
        VALUES ('Pedido', DATE_FORMAT(CURDATE(), '%d/%m/%Y'), NEW.Cantidad, NEW.Codigo, inventario_actual);
    END IF;
END //
DELIMITER ;