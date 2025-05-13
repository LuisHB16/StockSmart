-- Trigger para actualizar el inventario cuando se cambia el estado de un pedido a "enviado"
DELIMITER //
CREATE TRIGGER actualizar_inventario_pedido AFTER UPDATE ON venta
FOR EACH ROW
BEGIN
    -- Si el estado cambia de "Pedido" a "enviado", no es necesario actualizar el inventario
    -- ya que el inventario ya se actualizó cuando se creó el pedido (gracias al trigger restar2 modificado)
    
    -- Este trigger está aquí para documentar que la actualización del inventario ya ocurre
    -- cuando se inserta un registro en detalle_venta con el trigger restar2
    
    -- Si en el futuro se requiere lógica adicional cuando un pedido cambia a enviado,
    -- se puede implementar aquí
    
    IF OLD.Status = 'Pedido' AND NEW.Status = 'enviado' THEN
        -- El inventario ya fue actualizado cuando se creó el pedido
        -- No es necesario hacer nada adicional
        SELECT 'Pedido enviado' INTO @mensaje;
    END IF;
END //
DELIMITER ;