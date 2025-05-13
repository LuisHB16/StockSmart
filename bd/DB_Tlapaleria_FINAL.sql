-- MySQL dump 10.13  Distrib 8.0.34, for Win64 (x86_64)
--
-- Host: localhost    Database: tlapaleria
-- ------------------------------------------------------
-- Server version	8.0.35

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `categoria`
--

DROP TABLE IF EXISTS `categoria`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `categoria` (
    `categoria` VARCHAR(50) NOT NULL,
    PRIMARY KEY (`categoria`)
)  ENGINE=INNODB;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `categoria`
--

LOCK TABLES `categoria` WRITE;
/*!40000 ALTER TABLE `categoria` DISABLE KEYS */;
INSERT INTO `categoria` VALUES ('construccion');
/*!40000 ALTER TABLE `categoria` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `clientes`
--

DROP TABLE IF EXISTS `clientes`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `clientes` (
    `Id_C` INT NOT NULL AUTO_INCREMENT,
    `Nombre` VARCHAR(30) DEFAULT NULL,
    `Apellido_P` VARCHAR(30) DEFAULT NULL,
    `Apellido_M` VARCHAR(30) DEFAULT NULL,
    `Direccion` VARCHAR(100) DEFAULT NULL,
    `Telefono` VARCHAR(11) DEFAULT NULL,
    `correo` VARCHAR(100) DEFAULT NULL,
    ContraseÃ±a VARCHAR(16) DEFAULT NULL,
    status VARCHAR(30) DEFAULT NULL,
    PRIMARY KEY (Id_C)
)  ENGINE=INNODB AUTO_INCREMENT=3
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `clientes`
--

LOCK TABLES `clientes` WRITE;
/*!40000 ALTER TABLE `clientes` DISABLE KEYS */;
INSERT INTO `clientes` VALUES (1,'Cliente Mostrador','Cliente Mostrador','Cliente Mostrador','Cliente Mostrador','1234567890',NULL,NULL,'Tienda'),(2,'asd','asd','asd','asd','3317359231','Null','Null','tienda');
/*!40000 ALTER TABLE `clientes` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `detalle_venta`
--

DROP TABLE IF EXISTS `detalle_venta`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `detalle_venta` (
  `Id_DetalleV` int NOT NULL AUTO_INCREMENT,
  `Id_Ventas` int DEFAULT NULL,
  `Codigo` varchar(100) DEFAULT NULL,
  `Cantidad` double DEFAULT NULL,
  `Precio_U` decimal(10,0) DEFAULT NULL,
  `Unidad` varchar(30) DEFAULT NULL,
  PRIMARY KEY (`Id_DetalleV`),
  KEY `fk_detalle_venta_ventas` (`Id_Ventas`),
  KEY `fk_codigo` (`Codigo`),
  KEY `Unidad` (`Unidad`),
  CONSTRAINT `fk_detalle_venta_ventas` FOREIGN KEY (`Id_Ventas`) REFERENCES `venta` (`Id_Ventas`)
) ENGINE=InnoDB AUTO_INCREMENT=6 ;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `detalle_venta`
--

LOCK TABLES `detalle_venta` WRITE;
/*!40000 ALTER TABLE `detalle_venta` DISABLE KEYS */;
INSERT INTO `detalle_venta` VALUES (1,142,'cem-apa',1,225,'saco 50kl'),(2,142,'kil-cem',1,5,'kg'),(3,143,'12-12-4',5,160,'PIEZA'),(4,144,'kil-cem',1,5,'kg'),(5,145,'MORT-APA',1,175,'saco 50kl');
/*!40000 ALTER TABLE `detalle_venta` ENABLE KEYS */;
UNLOCK TABLES;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50017 DEFINER=`root`@`localhost`*/ /*!50003 TRIGGER `restar2` BEFORE INSERT ON `detalle_venta` FOR EACH ROW BEGIN
    DECLARE equivalencia INT;
    DECLARE producto_codigo VARCHAR(30);
    DECLARE estatus_venta VARCHAR(30);

    -- Buscar la equivalencia en la tabla unidad
    SELECT Formato INTO equivalencia
    FROM unidad
    WHERE Unidad = NEW.Unidad;

    -- Obtener el estatus de la venta asociada al detalle
    SELECT Status INTO estatus_venta
    FROM venta
    WHERE Id_Ventas = NEW.Id_Ventas;

    IF estatus_venta IN ('Venta', 'Pedido') THEN
        -- Intentar encontrar el producto directamente en la tabla producto
        SELECT Codigo INTO producto_codigo
        FROM producto
        WHERE ClaveAlterna = NEW.Codigo;

        IF producto_codigo IS NULL THEN
            -- Si no se encontrÃ³ en producto, buscar en subproductos por ClaveAlterna
            SELECT Codigo INTO producto_codigo
            FROM subproducto
            WHERE ClaveAlterna = NEW.Codigo;

            IF producto_codigo IS NOT NULL THEN
                -- Si se encontrÃ³ en subproductos, buscar en producto
                UPDATE producto
                SET Existencia = Existencia - (NEW.Cantidad * equivalencia)
                WHERE Codigo = producto_codigo;
            END IF;
        ELSE
            -- Restar la cantidad ajustada por la equivalencia a las existencias del producto
            UPDATE producto
            SET Existencia = Existencia - (NEW.Cantidad * equivalencia)
            WHERE Codigo = producto_codigo;
        END IF;
    END IF;

END */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;

--
-- Table structure for table `eliminados`
--

DROP TABLE IF EXISTS `eliminados`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `eliminados` (
  `Codigo` varchar(30) NOT NULL,
  `ClaveAlterna` varchar(50) DEFAULT NULL,
  `Nombre` varchar(100) DEFAULT NULL,
  `Marca` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`Codigo`)
) ENGINE=InnoDB ;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `eliminados`
--

LOCK TABLES `eliminados` WRITE;
/*!40000 ALTER TABLE `eliminados` DISABLE KEYS */;
INSERT INTO `eliminados` VALUES ('12','asd','asd','Apasco'),('6','com-500','cam','pretull'),('7','asd','asd','sadas');
/*!40000 ALTER TABLE `eliminados` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `eliminadosc`
--

DROP TABLE IF EXISTS `eliminadosc`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `eliminadosc` (
  `id` int NOT NULL,
  `Nombre` varchar(30) DEFAULT NULL,
  `Telefono` varchar(11) DEFAULT NULL,
  `Direccion` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `eliminadosc`
--

LOCK TABLES `eliminadosc` WRITE;
/*!40000 ALTER TABLE `eliminadosc` DISABLE KEYS */;
INSERT INTO `eliminadosc` VALUES (19,'Cliente Mostrador','1234567890','Cliente Mostrador');
/*!40000 ALTER TABLE `eliminadosc` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `eliminadosub`
--

DROP TABLE IF EXISTS `eliminadosub`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `eliminadosub` (
  `Codigo` int DEFAULT NULL,
  `ClaveAlterna` varchar(50) DEFAULT NULL,
  `Nombre` varchar(100) DEFAULT NULL
) ENGINE=InnoDB ;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `eliminadosub`
--

LOCK TABLES `eliminadosub` WRITE;
/*!40000 ALTER TABLE `eliminadosub` DISABLE KEYS */;
INSERT INTO `eliminadosub` VALUES (6,'cas','hfgaus'),(11,'caso','saco');
/*!40000 ALTER TABLE `eliminadosub` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `empresa`
--

DROP TABLE IF EXISTS `empresa`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `empresa` (
  `Nombre` varchar(100) NOT NULL,
  `RFC` varchar(13) DEFAULT NULL,
  `Telefono` varchar(20) DEFAULT NULL,
  `Direccion` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`Nombre`)
) ENGINE=InnoDB;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `empresa`
--

LOCK TABLES `empresa` WRITE;
/*!40000 ALTER TABLE `empresa` DISABLE KEYS */;
INSERT INTO `empresa` VALUES ('Tlapaleria Y Materiales Alvarez','Por definir','3325259582','Av. Juan Manuel Ruvalcaba 1034, Santa LucÃ­a, 45200 Zapopan');
/*!40000 ALTER TABLE `empresa` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `movimientos`
--

DROP TABLE IF EXISTS `movimientos`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `movimientos` (
  `MovimientoId` int NOT NULL AUTO_INCREMENT,
  `Movimiento` varchar(50) DEFAULT NULL,
  `Fecha` varchar(50) DEFAULT NULL,
  `Cantidad` double DEFAULT NULL,
  `Codigo` varchar(30) DEFAULT NULL,
  `inventario_antes` double DEFAULT NULL,
  PRIMARY KEY (`MovimientoId`)
) ENGINE=InnoDB AUTO_INCREMENT=5;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `movimientos`
--

LOCK TABLES `movimientos` WRITE;
/*!40000 ALTER TABLE `movimientos` DISABLE KEYS */;
INSERT INTO `movimientos` VALUES (1,'Venta','12/12/2023',5,'12-12-4',8),(2,'Ajuste','16/12/2023',80,'cem-apa',0),(3,'Venta','18/01/2024',1,'kil-cem',80),(4,'Devolucion','18/01/2024',1,'kil-cem',80);
/*!40000 ALTER TABLE `movimientos` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `producto`
--

DROP TABLE IF EXISTS `producto`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `producto` (
  `Codigo` int NOT NULL AUTO_INCREMENT,
  `ClaveAlterna` varchar(50) DEFAULT NULL,
  `Nombre` varchar(100) DEFAULT NULL,
  `Existencia` decimal(10,2) DEFAULT NULL,
  `Marca` varchar(50) DEFAULT NULL,
  `Precio` decimal(10,2) DEFAULT NULL,
  `Descripcion` varchar(1000) DEFAULT NULL,
  `Granel` varchar(10) DEFAULT NULL,
  `Unidad` varchar(30) DEFAULT NULL,
  `Imagen` longblob,
  `Categoria` varchar(50) DEFAULT NULL,
  `UnidadC` varchar(30) DEFAULT NULL,
  `ExtMax` int DEFAULT NULL,
  `ExtMin` int DEFAULT NULL,
  PRIMARY KEY (`Codigo`),
  UNIQUE KEY `idx_producto_ClaveAlterna` (`ClaveAlterna`),
  KEY `fk_Producto_Proveedores` (`Marca`),
  KEY `Unidad` (`Unidad`),
  KEY `Categoria` (`Categoria`),
  KEY `fk_producto_UnidadC` (`UnidadC`),
  CONSTRAINT `fk_categoria` FOREIGN KEY (`Marca`) REFERENCES `proveedores` (`Marca`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_categoria2` FOREIGN KEY (`Unidad`) REFERENCES `unidad` (`Unidad`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_producto_UnidadC` FOREIGN KEY (`UnidadC`) REFERENCES `unidad` (`Unidad`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `producto_ibfk_3` FOREIGN KEY (`Categoria`) REFERENCES `categoria` (`categoria`) ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=13;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `producto`
--

LOCK TABLES `producto` WRITE;
/*!40000 ALTER TABLE `producto` DISABLE KEYS */;
INSERT INTO `producto` VALUES (8,'cem-apa','CEMENTO APASCO',80.00,'Apasco',225.00,'CEMENTO APASCO','no','saco 50kl',_binary 'ÿ\Øÿ\à\0JFIF\0\0\0\0\0\0ÿ\Û\0C\0		\n\r\Z\Z $.\' \",#(7),01444\'9=82<.342ÿ\Û\0C			\r\r2!!22222222222222222222222222222222222222222222222222ÿÀ\0\0\0\"\0ÿ\Ä\0\0\0\0\0\0\0\0\0\0\0	\nÿ\Ä\0µ\0\0\0}\0!1AQa\"q2¡#B±ÁR\Ñð$3br	\n\Z%&\'()*456789:CDEFGHIJSTUVWXYZcdefghijstuvwxyz¢£¤¥¦§¨©ª²³´µ¶·¸¹º\Â\Ã\Ä\Å\Æ\Ç\È\É\Ê\Ò\Ó\Ô\Õ\Ö\×\Ø\Ù\Ú\á\â\ã\ä\å\æ\ç\è\é\êñòóôõö÷øùúÿ\Ä\0\0\0\0\0\0\0\0	\nÿ\Ä\0µ\0\0w\0!1AQaq\"2B¡±Á	#3Rðbr\Ñ\n$4\á%ñ\Z&\'()*56789:CDEFGHIJSTUVWXYZcdefghijstuvwxyz¢£¤¥¦§¨©ª²³´µ¶·¸¹º\Â\Ã\Ä\Å\Æ\Ç\È\É\Ê\Ò\Ó\Ô\Õ\Ö\×\Ø\Ù\Ú\â\ã\ä\å\æ\ç\è\é\êòóôõö÷øùúÿ\Ú\0\0\0?\0÷ú(¢\n(¢K\\ßµ´\ØaÝ\Ë.Ilg\0z~u\Ã^øº\æ\nM©Ï»û¨\çúVS­»\Ò\ÃÎ§Â[$@ú\Õi58N$ºO¡f¼Vo\Æ\ígúþ¹ª\í\ân»-\Éÿ\0y\ë\']\×²³\è{;\ëºdz\í²\ß\ÈUøIô\åû¾s}üMx\Ùñ%\Çð\Ãú\äÓoq\å¯\Ñj%,¢¯_\Ìõ\Ù|advú\íÖ¸sñ\ÓJòHnô´Db»¢\\C\ç\\\×ö\Å\ì\ÆóeYF\Ñþ\æ:¡&\î|ÿ\0\ÏFþu¥*²\Ô\å\Åa>®\Ò}O¦¼9ñ3\Ã(¹[K\ã\ã-½\Êùl\ßN\Äû\ík\â«¹Ïôûß¾[°\ë\ÃÊ¾\Ô+¡3¡h¢b\n(¢\n(¢\n(¢\n(¢<\ç\âMñ´>÷úÿ\0Ö¯8·Ó\Ì>lB\Û3rrk½ø\Èu%f0ø\à¯\ÚvwËÅrjó«|lú<½5F*:7Ô¢¶Ò²	ò,GAÖ§þÏ¸\n(,ÁH\Ý\Ê\ç¦GjQ;An«òme<õ\ÏzVÔ¥l05m\Ê\ÌÀ¬´=&\ë_D\Ð\È\Ò#H\Î\ÒÍf­[XLg\ÍP1Y5	|\Æe ]úóÞ¬Cq)P`À\ïÖKÚ½¿\á¿Ì{_²MjKY\Æ@\í\Íy¾§ÿ\0sÿ\0\×Fþu\è²3<#B¸\Æ~µÈµõ¬)42d\Ë\ç9&F;s]®xY¤§õg7¦ºÝu\Å\Ägÿ\0ö\à\é_X\Æ\ãT·r¸X\çvx\Ç\Í_mÚ%QLAEPEPEPEP|V\Ô-l5St\Ò\0\Ñ\0»7s\î+Í¤\×ô\Æn\ìÿ\0À\ì\Õ\Øüvÿ\0¾ÿ\0\\\ÍrV·]¬6,[H{ß½H8=$W$\á\Ûhô(\â\êÓedV:Ý\éop\àj?¡¥\Z\Ä$M:v gs\Ç\àµqµ;	 dki!¥[\Ä;8`\0#\åG°É¥¸\×\ìd`7³lc<C#9P	\Èú\ÔòG±o]ý²ö\Ë,>z\él\"\Î7³±\\ýqM>\'\Õ\Ù\Û÷\ëS>±ö-ktd\Ü@\"r<¡­õü+`T=AÁªPb%­ü\ìÚ´ñ-\ÝÎ­i\Ãl±\É*©Ú8\ÏlH\×6\Z¡£\0ò\å\É/\Êq	\ë\ÓsXúg °\Ïü÷O\ç[0iº\Ã1@ \È>h³Aßpq\×#¦\Ø\É\ÎS\ÖN\æ,¬%Z5eó	\'9by\É9þöJý\Ñô¯õ\0R\äÆªÁS\ÉD\Üy\01\ês\Î~\Å}¿t}+hls\Ïqh¢²( ( ( ( ø\éÿ\0!kþÁ®:mBú\à«-tM¸.Hõ\é¸|\Ý?Zô\ë\ÚÍº¥\Â\Ã\ä\Æ+\çÿ\0\Õ^x~Nü#ÿ\0¿GükN7\Ôè´+}³PVùc$r£s\Ê\0o8\'þi7Ýª®\Ë8\0Z@Û°_\Ïôoþ)ü\Äþýñ¥þ	ÿ\0~\Ïø\Ô\Þ=Ê´»×©y{°0=B\É\Øñô\í\×Þ².!{y69\ã99®±|1ÿ\0ß£þ5b\Û\á\êù \Üjû±ÇOò§\Ï\Ô\\²}	\é/}¬­\Û/\î-~rÝ·v\×ð¢k\Ètøõ{I!-v)C\ã ô\Æ=«\Ó\"\Ó\í´ý9mm\"Æ¾Iõ\'¹®#\Ä\Z4ó\î\Üb¦ð3ù4\å©|GC\Ô\ïmf¿\Ä+*·\n\ã\ä\ãLþµ}¿t}+\ä«¸¿´\0Ãþ§\ßý\êú\Õ~\èúWT\ZkC¢i\ê-QVfQE\0QE\0QE\0QE\0yï¿\ä:\ëÿ\0Z\ãdq.7M\ÐdS\Ü\×c\ãÿ\0\æÿ\0®K\\X\äð\Æ\Â?®ül\é³pV!g¸g\ÆOH6¸Þ¥¼üg\Ýb\Öns·\ç$ð\Ýÿ\0\Ze«\ãZ¤§}n\\Q\ÍL:U4k¿\"÷\äÕµÝ°n\Æ\îø©gD]Å\'¥qº¹]òqþ\ÏZ\ì\îy\0w®+UW\Ýsy¡\ZG\âCt¶%A;÷\ß5ôý\Ñ_8Y²nù5ôrs\Za]T:c\çqôQEtaEPEPEPEPx´\ç^\Ú5þUÆj\Ë?z\\rAò\\SÓ¥v>- \ë\Ó{\"ÿ\0*ò9/.ù&E1F\n\ß# ö\Ä\Õ\ä\Ê\Äb\Æ\Ý\àµõ½\Ùa±(`Q\é\ÔUkð\ÝÄ·]4 .\Þ\ìO\êM]Õµ¸tµ	·ÌBg§¹¨k[#Jxº^Ò£²6PÕ\é^s/µ9_+8uZµg\â\ÝB~\ÉÓ¸#iü\Å7M\Ë1¡\ÍgS»k\Õy®Xd ñ]\r¾­o«X c¹~ò«\\Æ¤\Ò7\å\çx\éR\éÓf¢ôb\Øq\\X\çñ¯£\âÿ\0T\îù»NÝ¹ñ\æùxóz\ç½}Í¼G\Õò®ªLñ&¢+s(¢\0(¢\0(¢\0(¤\Í!a@q\â\Ãÿ\0\r\Ç\Ñô\\T\\°©\'\'Æ»\È\'ú/òÆjÍ¤\Úd*rG\Ì1\í\\¿3±\Ò\ãM\Æ<öùZXZ\éia7¶Nzf¸¯µ1,\Ç&YF~\é]Ñ;Wà«©\0Ê¼ó\æOFSùUO[v5(rF+\Ý\Ôô¤ij¿=¬J:dñU®¼)i4d\Ú3C&8\îSùóW,\îã½²Q®¼¼÷>£%\ìóûÈºÇªû{T&\îtû\nR\ÜW/trv\×\èÚ\Ü\n;$OQÞµ/¹V\Ú·sòY¾!¸\çX¸x+À\È\ïE\ìÆ¶\Èòº\'¹eñW.\Ï-MT\"\îÐ¿¦,­\æ#\êÍõô§üzÁÿ\0\\\×ùW\Î6·3 \É\Í}dÙ±·>±/ò­§v.\í¦\ËTQ+ \ä\n(¢\n(¢\nBqKHE\0Uw^¨O}:µEi\É\ê¬ö¡»Pø¢\ÇV\Ô/\Ú\êpù\0®O®+NÕ\â}6b=?ò\Í{ki\ê{Tm¥©\íYJ[¹ª«$¬xyi\ì£TkI\ãAÀ\Ì,\ê+\Öaýû\\ÅPyS\ë_G¶µA.o(Ä£}T\ZJ\Ó3­\ËVG\Í\Ö\Z\åÆ-*\Ã#5¿¤\ës\ê2\\­\Æ\Ï+\Ë\Æ\Õ\ì3øD¸ÿ\0[¥\Ú7¹ÂªZ\Z#,v~R·Q²\çò4¥F\æT£(Yshº%¥\Ú\ëx]ò­=J\ê8¦\ÆG¶Zõ<ch»m&¸G@N?0j\ß\Ã\r6þ\ä\Ïww{,©.£ù-K¥&Í°Ñ§A;n\Ï(µÍ¸Fyw9ônªDöp*È§¨\àûWmð\ËC¶`\Ë\ÌG÷¤&ºKM``+Zp\å4«YÔ·¾úS©\ÍP·¶ÙM_Q\Ð\ÄuQ@Q@Q@&)h \Ú)6u\0Ý\ÐRÇ¥>\0Ä¾Hô©h |¤0(©«WÔO¹µó>[G\ß\æ¾\Ò\ÇNj\\UØ¤\ì®V±ñ^¨j\ãL\å>\ï)\Þ\"]½vø®FJ\åôm\Ö\â\'R{Sm-\Ã\n±!|ÃNy\é\Ï\ãYºc\Ö!®qjºÜ¢#Æ®®\ËnùHùq\Åj\Ôn\ì\Ì!Rn\nSZ¾Ç¡ªN¯.µð\çR\Êú$¶\Þg\Ódf{\ß3\íw\îÈ¼ü½ù8ë¿ª\Ù\êúi²\\\è÷2[@¯\Öùcf|\r²nVÁ3\×¹÷4\çv\Øô*+Ïµ\ëWó_%¸\Þ9\Þ\Ç\Ët¹£	0NIzÖtWJ¿\Õ\ßT»{¶¸xgfs´\îÚ¿\Â\àjRW¸Ô\íc°¢*K\n(¢\n(¢\n(¢\n(¢\n(¢\nCE\0´QE\0QE\0QE\0QE\0ÿ\Ù','construccion','saco 50kl',100,10),(9,'12-12-4','ARMEX 12-12-4',8.00,'ARMASEL',160.00,'ARMEX 12-12-4','no','PIEZA',_binary 'ÿ\Øÿ\à\0JFIF\0\0\0\0\0\0ÿ\Û\0C\0		\n\r\Z\Z $.\' \",#(7),01444\'9=82<.342ÿ\Û\0C			\r\r2!!22222222222222222222222222222222222222222222222222ÿÀ\0\0\0\"\0ÿ\Ä\0\0\0\0\0\0\0\0\0\0\0	\nÿ\Ä\0µ\0\0\0}\0!1AQa\"q2¡#B±ÁR\Ñð$3br	\n\Z%&\'()*456789:CDEFGHIJSTUVWXYZcdefghijstuvwxyz¢£¤¥¦§¨©ª²³´µ¶·¸¹º\Â\Ã\Ä\Å\Æ\Ç\È\É\Ê\Ò\Ó\Ô\Õ\Ö\×\Ø\Ù\Ú\á\â\ã\ä\å\æ\ç\è\é\êñòóôõö÷øùúÿ\Ä\0\0\0\0\0\0\0\0	\nÿ\Ä\0µ\0\0w\0!1AQaq\"2B¡±Á	#3Rðbr\Ñ\n$4\á%ñ\Z&\'()*56789:CDEFGHIJSTUVWXYZcdefghijstuvwxyz¢£¤¥¦§¨©ª²³´µ¶·¸¹º\Â\Ã\Ä\Å\Æ\Ç\È\É\Ê\Ò\Ó\Ô\Õ\Ö\×\Ø\Ù\Ú\â\ã\ä\å\æ\ç\è\é\êòóôõö÷øùúÿ\Ú\0\0\0?\0÷ú(¢\n(¢\n(¢\n(¢\n(¢\n(¢\n(¢\n(¢\n*Ú¬,c3+KyHw>~a\æ\ìf\à!\ë\å!ùÿ\0iO ü\èwu\Þ~aESµ(­cPA±\é­\ÄjQEQE\0QE\0QE\0QE\0QE\0QYwú²Ch·?tmTcÀ,~§¥\0h»¤H^F\n£IÀD\Ü\ÏwÅ \Ùy\äGû#¿\ÔñU¢±¾95K\ç\0elU>¡Oñ«²Ý¤yMó#a`ON(oö:H\æ\çyX|\í\é\ÏÒt[H\"5l\n\ç\æ\à`÷\ë\ßó«Ñ 5E\è Yº #d+\æÈ¬\ã8T\êÇ·Ó­\0ZY¾Qû¹SºGJ*.\á\Ò\äó\åc½ÀÒ¢)(¢\0(¢\0(¢\0(¢î±¡w`ª9$@ªw:­²2e\Ü6)\Ë\é\Îj£jä¦\Ë\Î\Z\êAò÷Gñ*K\Ñlü¶¼{\ï.x÷ÃôÁ8ô W-l½\Ô9u­±ÿ\0jx\ã\Üÿ\0\á\ÍK,À¶ñ DiÀÿ\0dÿ\0\Ùiswm\×71\ãAý\éø\ÓR\æ+«\ØB6v#³ÁSÀ\äv\êhB³µU¶$P	÷Á^sÁü*K«ô·o)Í\Ôô¤ö-\å\Õs°\"^\Çþ\à\îÚ \î5¥	¢ö&l¤c{(\Ï\àÖ­¾¨\Ê\í\åc_º§úsU^\Ü\Û\\\Û\Ã¢Wy<\Æý\â\0\';lûU\Ö-D9\Ë@ã¼Bþ})qþù¢ªA¨\Åwm(x 6$\ê\r£ERQE\0QE\0TWvöPn%X\Ðw\'­a\ß]jö3@¦\Ò\Õ#f\ã÷ñ\Øv¹~\ã[µM©h~\Û3ý\È\à!³õ=\0¨\ã\Ó./\\Mª¸q\ÕmSýZý¼J[O\Ø\ØZ¤vqigÍ\á=Iõüj\ÇÚ§µ8¼M\Ñÿ\0\Ïx\Çð%\ê>½(=\Ùòtù\Êl¶\Çn*H#\Û\Å\èòZöDÀyn¬º(`r,-Ö¡³L·\r÷bN¿S\è=\Í,\É\"CI#E,\Ç\0W3¨\Þ\ïxRX¡)µn|\ìA\Ï\È3©\â§\Äú\Îlî§ü±ÿ\0\ì\ÍZQÁ¥fiP\ÊÜIÀ\ÐP-\Ì\Í\ÖK\Û5\àT\Ø4HÛ·\Äe\Ûø¿tJ¡T*\0\àÚ³,\ì\ÒksÒþbp~bO>½{\Õv½\Ômeû(1^\Ü@PP¨\ì[°\Â\És~ iOÂ±*K19\àzóV\Ö[\ÖÞ¨·#\êÞ§Û¥V\ÐôÛ¨L×¯\ærIC\Ø\nÛ \n\ìª\áVsýóE0&¢)(¢¨\ê\Z¥¶iÜn4w>P\Òp2k\Z\ãYy¦k]\"u8\á¥\'Gõ=þ±\í­5ýjn/.TYI)h¬\×\äb\Ýf\ÐX\\YÆ«g_dGü{¸\Ú~£³}Fh®«0º¿\Þ]ög*º½X\ÕyÓ?\ç£$÷\ÓýjõR\Ôy[uþõ\Ä¡\Ïô ´\Çu»P2I<\n¯{¨AaiXcFYÏ 4·\Z\â9có\\­¢·\É¼\ß\é@Èµ)Lñ¼úhñ+n31*³09W¹\Ïñ:n\ßn¶SvXF\äL\ïÁ \îs\î·\rvÀ\Ý\Þ\Ê$;	\Ý¾µ&«&À0¼<¹þ´¹Q\Ã\Çª \éY\Ú\ä¾U¬g8ù\Ë\ß(\ÍüÀ¨o-ZÀ½Ý¾¢\Ö\Ñÿ\0\ÏPÑ`;~öÞµ¨\Ú=ôQC§ ±[[?\Ä=\ÆF=\èZ®\âK}?\ä#]\Ça¯J¿kg\r¤{\"^¼³c\êOzT*P\0\0;S¨\0¢(ýóE÷\Í\ÄMER©I©f84øt\Ý<\å};K\r\'7;\\^0ù§øz\nÓ¤=\r\0QÑ²tRzÉ©\îma»Ë5u\ê29\ÔÆ¡\Ñÿ\0\äh}bSúU\î!µ¦E5,\Ç\0P\0·\Ö<¡k¸ð±ý\êcÑ¿}\Íek:\ê4P¶¦Y H\ì\Ã	Á6q\Ï\'\ÏZû[¸\Õfû%J±7EN$zü\îy§\é~6º\â=\ã$\í\Ëu_\Ý\ÆU?\Þ?1\äÐ?M·R\æ\rÄ·1½k£µ´\Ê\æO©=\Í\Ù\Ã9\Ü\éN\ÖB9ªNÚ­¤ÿ\0$kwm\Û,Uõö4\0ÿ\0I\åh\Íÿ\0LX~|Zh»N°µ·Ui\'ò\Õc>ó`~Þ°|A\â8.\áIµw¸ºc|\Ç\ÄX9;½ø®\ÃN\Ì,\Ò\Îÿ\0~gû\ÍþÚ!¶\Ó\åqy¨0q\Ìq¹\Ó\ÔûÖ¥P0¢(\0¢(û\æ\ï)(¤0 ô¤\'\'¥a\\\ëÓ½«#©\Û-\Ós_O\ïj\0 Õ­\ì4{\ÃKq$J#>Y=;z¥w¡j\Ú\Ä\Ð\Í{vGL2#±Ø·¹©t­%ü>Ò¿ö\Ï0\ä\Ü*\âU}3­\ë{¨ntR+c¨\î>£µ\0G§\é¶\ÚdU´{s÷ò\Ì}Iª\Ó\Ü\Ç I/T\0u%¿ÀV­szÍ½¯bº\Î!¶\ÂD£,\îÄô\Ï\ç@$Q³\ÈÁQFI\'\0V1\ëYb¥­\ì?~\'²úzH\ìnµYV}Pyp\ì\Ôñõs\ÜûV\ØP \0ÀnCkkªF½\0þub(QE\0QE\0QEýóE÷\Í\ÄMERª\é³j~T?jxm93$|4=]¶µ\Î\Ý ·b¨\ÅOE\0ªWv°ÈS´ª8h\×üv²u}Fþ\ã\ìñ\Ê-¬±û\ÉPü\ï\ì=>´ÿ\0	¤÷\Ù\évò^L \\ O,ÿ\0´À\àþo\Ã:6£ng¾\×\Z)µ	_*\ËÈ}mY\Ø\ÛX@ ¶F\É>¤÷5nX(¢QE\0QE\0QE\0QE\0Bÿ\0|\ÑCýóE1QEQE\0QE\0QE\0QE\0QE\0QE\0QE\0QE\0Bÿ\0|\ÑCýóE1QEQE\0QE\0QE\0QE\0QE\0QE\0QE\0QE\0Bÿ\0|\ÑE\Äÿ\Ù','construccion','PIEZA',NULL,NULL),(10,'MORT-APA','MORTERO APASCO',80.00,'Apasco',175.00,'MORTERO APASCO','no','saco 50kl',_binary 'ÿ\Øÿ\à\0JFIF\0\0\0\0\0\0ÿ\Û\0C\0		\n\r\Z\Z $.\' \",#(7),01444\'9=82<.342ÿ\Û\0C			\r\r2!!22222222222222222222222222222222222222222222222222ÿÀ\0\0\0\"\0ÿ\Ä\0\0\0\0\0\0\0\0\0\0\0	\nÿ\Ä\0µ\0\0\0}\0!1AQa\"q2¡#B±ÁR\Ñð$3br	\n\Z%&\'()*456789:CDEFGHIJSTUVWXYZcdefghijstuvwxyz¢£¤¥¦§¨©ª²³´µ¶·¸¹º\Â\Ã\Ä\Å\Æ\Ç\È\É\Ê\Ò\Ó\Ô\Õ\Ö\×\Ø\Ù\Ú\á\â\ã\ä\å\æ\ç\è\é\êñòóôõö÷øùúÿ\Ä\0\0\0\0\0\0\0\0	\nÿ\Ä\0µ\0\0w\0!1AQaq\"2B¡±Á	#3Rðbr\Ñ\n$4\á%ñ\Z&\'()*56789:CDEFGHIJSTUVWXYZcdefghijstuvwxyz¢£¤¥¦§¨©ª²³´µ¶·¸¹º\Â\Ã\Ä\Å\Æ\Ç\È\É\Ê\Ò\Ó\Ô\Õ\Ö\×\Ø\Ù\Ú\â\ã\ä\å\æ\ç\è\é\êòóôõö÷øùúÿ\Ú\0\0\0?\0ùþ( ( ( ( ( ( ( ( ( ( ( ( µt½SÖ¥d°²\ãg\Þ*0«õ\'Yu\ì\ß=;À\â\å\Æ<Ù\Î,s´\è5g\ÉR¦\êK\\\r|G1Kh`¼®ýóÓ·øO¨6>Ñ¨\ÚF;ùj\ÏüÀ®¾}nø\ê4o»\Ï\ëUN©}!Á¸o\ÃùW\Ä\Èõ¡I\îÑ\Â;5#\í\Z¼\Ï\ë\åÀùZü0ð\Ä y\Ó^Iÿ\0]&ùP5\Ì\ï\ÓH\ßV&2y<^}\Í\ãG«ü\røA<\å­Ñ\Çtÿ\0üUq:Æ¥Ãª\\\Ã\r¢¤H\ä(x\\\×W\"08 ¦°µù]¾k¿\'9¾mt<\×<#\È÷g+ªhVöÑ³\Û\Êr«¸\äýk®\ÏQ\â\Îl\Ï6þUÆµ\Õ^*-Xó¨\É\Éj6(¬\r( ( ( ( 	?ô\Zõ\Ï\Û\É?t¸Q9¥#\Üw¶+\Éþ;^µ\áùid\ÏL $n)û\í\êkºn6GvJ5nÝ´\êk^Ü°qi8]¡\ÐðxPAù\ÓwX+þ\ì 8m¦E\Ü\Ý\Ü}3Íh\Ë5\Ì$ù \çõ¨¼\ë`yº~gù\n\äTj½¢þ\ã\Ûöô\"uù£RIlV\\\0Ù°ü\ë\Ç¡«\å\ZG>cH6.>`qXi´þ>ý¿Â¥MN\Æ>²9ÿ\0u?Æ©a«?²\Ì\Þ2\ÞiüÍ«û±=¬ªmÀ\ã¯^¿¥s·\æ¨Þ/\æ\äqÛ¿?ZY±u\Ú>\Ñÿ\0~\Çÿ\0OVÛ¨\ê\ÌÏ}\Òrp};}xü«¯Bp¿:j\ëõGÕ¥^19^\Í\ß\î8\íEI³¸|eBÀ\×~õw´l°\"H\Õ\Æ>\ÅpG­wWwhóh+!(¢\ç7\n(¢\n(¢\n(¢\n(¢Ú½O)­¼?\í¶\'·P\ç8Àó<××¢Z:\Åk\á÷d«j©\èxüU\Óø´\"§Ã©®vñ\îK°¡!®yld\åx\æ¬5®¬\áM¾<«s\Ç<\Éÿ\0<\Z¨/6õ´cv\0<g9ô\ÏNµ4N®¡$´·Úª *Ây­yfú¿½^+¢û¶ò\è\Ì\ãÎ\Ù\Å\ãqù\Þ{~õZñ´Ñ¦\È/µ{óc\é\×FóF\ÚZ©m\ß0I\01øgñª\ÑòGÿ\0¶«W\nj÷mý\äNn\ÖI}\Äº4Ú¦§+À\Ò\Ï=:õ¬.Á\ïõ(\í\ÔeCe\È\èu5«<±\Ùêº o/\ÌONpù\ìx>ô\ë\É9r­\íú¡QQ\æ{_ô0u\ïJt\ÇHy\á«Î«Ñ¼A,2i\Ò¤û\×\r¿ZóªÂ¯Cz]D¢+#P¢(\0¢(\0¢(\0¢(k×´_¦±\á&å®\"¶\Û6\Ïñ1õ÷¯\"¯yðÓºÿ\0¨=\È\î}+:µ%M)E\ê\\!\ÝKc?þ\×þ_þýÿ\0õ\é\á\Ëóÿ\0ß¿þ½m`\Ò|ý	\à\È\Ã\×Ú¹³Ë\ï\ç7øV?[­üß\Z?\Ëø³)|ÿ\0\æÿ\0¿ýz<\r#uó\ã¾#\ã[v.Xº\äsþ°±ýEi(â­\Öþo\È\Ò8z2W\åü\Êzvi¥Y¼v\Ñ\à·ßY¾¦¹z\Ê+\Æïñ\n\í\æ\"Â¹\rmI\'\00FkÔ?=õ:=y-¡\Íj\ZcI»\íJBí¾k\Ì;×¯²ÿ\0Äü\\ýòAøMyvÂ¬\êk7sµR,¢*Ì( ( ( ( ¯}ðzøC4\Å9Át8\îkÀ«\Þt	\ÛÀ¡P\Ëj¤\Ü\Ö\Z\Òi]³c\ì\ê°yG9ÇqH-À9ófÿ\0¾\Ía¶·:¹Qwf\àn	\íþÿ\0ùÇ¯µi3\\Y\Ã3m\Ü\è\í\é\È\í\\f\êS¨\ì §\ï\æé½V O-~û¿û\Ç5N{»{U\r<\É=763E¾µ§JÁò,û:Eº\â\ìÚ¹¦[\ÈkhCÉ°\Ì3u®¹\î	\ãõc6\à¸$\â\Ü\ÕnP\r¥_M¨``>]¹;Nk\Ç;×±2i·¤¹/\ä0ÁM¸\à\â¼w½vPÙb¾1(¢\Ü\å\n(¢\n(¢\n(¢\n(¢½\ëH\çð%1\0]\ì\0vð^õô\Ø\ni_õ\éþ+F\ÈÚT®&~# Ûs#¨ýz\Ö\Ô÷­¤h\\N#X\ÕI\ÏÍ\éZ,ù5\Íø´·iýÜ¶~¼WJ*9Tw±k§_\ëFK\"±Ýi½I/u(·$¯\Ù?Z\Ñð\ã\ì\×gpÿ\0\Çô­×ºÌ$_2\Ü}\æQó\'¹\Çù\æ\ÎzXju)*n\ï©\Ìhº\ì\Ú|¢\Þ\á\Ø\Û1\ÚC\Ë?qþoT\Þ\\®\ån\0­C\â\Ø\àûE½\Ä;s:\Åz61úþHÝ\Óí¢23¦\Üg¨SGF\Î5%M»\ÛU\èKÝ¦\Þ1d\Ä\Ãs°9À>\ãõ\ì$]6\ãd~Yh#ð5\ã\ruPÙ8§y\\mQ]0QE\0QE\0QE\0ø\ã2\nØ²ð\Ý\Åð$Aõ¬\ËQû\Ê\ïü8POº\Þ6\ÜD+¾[yô\ÛM¹E\Ä\Ìp1Ô\ÖÑ mËk®dTæ³©MMY	ò]B%\Ò!(|\îÁ\év¬\é¨Y4J\Ã\ÍC¹9\ï\é^«ö[K\'7ÿ\0y¦\nh·\"\Ê\ÝI\êUÒ±x~\ÌnQqpkFx=©&x%Q\Èù]\ÓÁ\â»i\æB\Ò0S»\0ÿ\0:ô;¯\Z\rÛ\ZÐ#WÖ¨\'\Â-&	\Öky®£e9xa\ÄRxvsRS§\î\Å\èy¦®Ú³YÅT;bO@~lÎ®\ß[C0\Ã\'ü³P=>µ\èü3[k\É.ð<¯\ÞX³nªÚ\ÃJes¨\Å¨\ÆØ­\ÏõjFw6\ÃF4e)§«ü\çq\È\Î\Ò2£g\æ\Îx\â¼\Ñ\ì\äNµô}§Â\í\Î\çÔ¥P\0fkôûp\Ø\\\\WE(8\îmZ¯´k\Èùñ +\Ô\Ôdb»¿hv¶*|±\Ípó.\×\Åj`GEPEPEPOõ\è\è+\Î`}\ê4%2(\Û4@7/×¢¼ONø³©®?Ö@ \Ð¦\ì«?)\"¼\Ü|U²4ññNÂ=:+\É#ï½¤ÃòQñKOõ üR\ÓÀ\ë@\Ï°KÑL S\Ó\áñf\ÕÉ/\é\èM\0{y·®7\Ä\ÑG5\ÈGñ\Â\ßxóX\ßÅ[\è\Ø*M\0q0u\Ë.{×\Üÿ\0¬­ý_[[\ëp:\×=3|\ÐtQE\0QE\0QE\0(¥V>´Q@\n$\ï\Z_:O\ï( Îû\æ¬ùòÿ\0|\ÑE\0|¿\ß4yòÿ\0|\ÑE\0Vó¤þù¤ó¤þù¢\0<\é?¾iZGþñ¢\0ac\ëIE\0QE\0QEÿ\Ù','construccion','saco 50kl',NULL,NULL),(11,'CAL-34','SACO CAL ',500.00,'PROMAPESA',83.00,'SACO CAL','no','SACO',_binary 'ÿ\Øÿ\à\0JFIF\0\0\0\0\0\0ÿ\Û\0C\0		\n\r\Z\Z $.\' \",#(7),01444\'9=82<.342ÿ\Û\0C			\r\r2!!22222222222222222222222222222222222222222222222222ÿÀ\0\0\0\"\0ÿ\Ä\0\0\0\0\0\0\0\0\0\0\0	\nÿ\Ä\0µ\0\0\0}\0!1AQa\"q2¡#B±ÁR\Ñð$3br	\n\Z%&\'()*456789:CDEFGHIJSTUVWXYZcdefghijstuvwxyz¢£¤¥¦§¨©ª²³´µ¶·¸¹º\Â\Ã\Ä\Å\Æ\Ç\È\É\Ê\Ò\Ó\Ô\Õ\Ö\×\Ø\Ù\Ú\á\â\ã\ä\å\æ\ç\è\é\êñòóôõö÷øùúÿ\Ä\0\0\0\0\0\0\0\0	\nÿ\Ä\0µ\0\0w\0!1AQaq\"2B¡±Á	#3Rðbr\Ñ\n$4\á%ñ\Z&\'()*56789:CDEFGHIJSTUVWXYZcdefghijstuvwxyz¢£¤¥¦§¨©ª²³´µ¶·¸¹º\Â\Ã\Ä\Å\Æ\Ç\È\É\Ê\Ò\Ó\Ô\Õ\Ö\×\Ø\Ù\Ú\â\ã\ä\å\æ\ç\è\é\êòóôõö÷øùúÿ\Ú\0\0\0?\0ï¨¥¢Z((©)\î£ ¨ÑWtt8e^Jz{1\ÅX d\Æ­)\áCºzÆ³^£#§­J)h ¢\0J)h ¢\0J)h ¢bÐ°\Ï!O8\ï[m¶yL\ZC¤ô<t>\â\ë­2\Æ\ËW\Úñ\Î7Ì¤úV¶¯\éþ ³Só¡\ìk\nÞ³f\ÐZ\\º5P]\Ðôü)\ÏtQ­ñ!ù\Éñ\éóõ¦µ¿0\Ü\äu\íTo!uò\Ù3$\ã\ê?úÕ]ML\Öt[S+¿6±_\Ïj¾¯$\ÞB\àöªP_!_\Ö\ÛÝ¶\Þ\nB{ªµ\Êp\ÞIB7Te\Ç\ÝÀ\Ç\åM\á\"Bû½\Ç?*g\è*+\Å[H\ÚY\" \Ë1<Qe-\èÁ2\éI\Î]\ÇÉ¥\Í17AP\ì9oC\ê=*bc`)\Ç<®j­«(±·\åR«S\ÈZ|ò\îKp\ì0»xÜ½\ê´¸=N\ÑU\î\î\í\å\Äx\ïVt¸DozsG<\Ü\\ªÚ£A\ì\Æ\ÙO¨\íYòE±nÜ¥\r+EÄsLõ¤\ÕQ!\0V\Ô\î\ås9\Ù+tR\âV\æ\"QKE\0¥\Å.)1@^-\Íðü§û­ú\ãú×Y^\\iwkw!ò;^­©Ú\Í.\æ\Ü}çõ\í^GpvÏ0p+º\Ö\çM}Zðþ¿o­Y\ä6%QC\ÔV¼±\'¸ *ñM7QK¿K8aÙ¥z¾­Eª\Ú,°\Þ+X©tf6\Õukò\ä\Õ\rµ\ëØ·\ï\È9=+Y_\ån	¯<ø«¬PI¥Ù¾\éX~ýð\îýOò­°øyW¨£\ZøÑ4º\çô]b&´i¯cóE\Zr;òs@ñl\é¦}®wY\Ø\Þh\Ïl\äñ\\V¢ý²\Ç\í\ZZ\Å\æþh\É\çó«º¾.-§óá¹¶±ô8\ëJöV9{(\ÉólóÂ\å\á¾ß©\ë0E[·z©{r\ÄykÁ5\ÊxWÅi_\ì»\Ù1p!¿\å ô\'\×ù\×SdªÍñ1XyÐS\r^\à§kXvÅ\å½ûÖ¬[¨:(ü1[6y1±\ä\ÖpFdðB!Lþus1¸¤9Á<A]²´¨S\\\Ö+®\Ç,÷Q\\Rb´ 1E¢b1@\r\Åyôµ±\Ö7nô#±\î?Ï­z~+ø\ìöó«°ü0+*\Êðf´eiCÁ*m\ÎFA\Å]\Ñuôk\Õ|7}ª+i¼\Øö0?/+\Ï5a\ÕR&c\Ôd;W\Îä¯¢;¯xl¼?\í¶Ið°·`H\'\'ò5\å/;\år\Å\ÎK1\É$Ö­¨\Éq\árF.°\0\èõ¬1p²\Æ\ã_WF*2Ý½O\Î#5_[#f\ÛX{[±KKyc/\ç)\Ïö¨\çñ\æ¢\Ñ[\Þ\ã£vª\æµ|=a¦¿®o¯¬\Í\ÃÁ8UÄ¬¿)\è}M:wðû0hô<J\á®\\YU\Æ\áhVr\ä|\ë©×Ê³fFðõ9\è\í®.¯X\Ý\çfù~¢½§N[C\Í\ÂË¨$*e§½q©¯[+M³¶²a4ù¿:Ï³¹5»Y\ÎVÝ¸}ýk\Ë\Ç\æÒl\Þ}W\ÃuhBN¤µ}`\Ó\íw·\'µk#0\0b²4{øu>+ò¥º\Ý\Åj¡\êk;U#(\É\Æ[¡.À6ý\Ã\\\æ+¢¼%l¥<}\ÜW=é§±\Ï=\Ä\Å§bU7S±E\0.(\Å-\0®Ç²5¬Yû¨[S\é]\Õp^=Eû}»¢`¼k:\ß4¥ñ£²L09¡§j1]^F¯9a»©¦\Í\r:¼r@¨u*qõÀ·\Ðô£7Mó$YE\ç\Ã\ßbyV63y\Ì1DgR@?\ëÿ\0^¡°²´¬¢e¤`,G\Ç$ó\Ô\ã©m¦JºÎÓ\æ6\"m\å\çv1\Ï\Zôh\â*Q\\°µ\íÓ¹\çbiF½G9\ÃV»õ\ìh\Øøv{\rò\Â;\æp\à\Ú1\î}*¤~\ÔX\î\ÝyÒµ\ì^T\Ò|¹\Ä[CG\Ú:÷¨4\rCe§]6F¹\Ý\àb\Ïp:©5\Ï$ª9N¤µ¿m\Îü.:¾\n.]õ\è@¾¾´5·½OÂ­XøZ\îugI\í_½¼²ú \ËÃ¬¾ITV\ÒO1bÀ<ô«ºB\ÚYj\é(ûb \è\"¿{\æ8\ã<Uû\n6~ÿ\0^\Ý4\Ô\Ùçµ ­oÇ±µ\á6ûMv>\ÚkY\"7\'Ð1\í]lE\Ãk\È\Îq\\ö]\n\ÞKQ~²Dd.\ÙWfHu\éÞº¨§G^3\æ+NA¨q[QwGZµk?iV6\ÜMI³#Ôk£}2\É\0P6\îCV~+z	\Ç=\Ä\Å¥¢¬1E-\0´b\0J\âü\nù6³q»\æ\\~U\Ú\â¸\ï\Ã!\ÎeLÆ¥8\ãÊ³ª¯iK\ãG$¨\ìA%\0m ¹Àc\í\ëK\r¥´\Ñù9V\'¹\ãx5öó\Ì\Êû\Ô\î=¯Ó·5jM=\'·¶$\Ú#v,d\à\Û\êk(>¶;y\êCThA£\Û2C¢©þ£np7°\Ïû	ÿ\0\Ä\ÖöL\êÙý\ÎA9\Ý÷6õý+B-&øHû2\ZW\ÈÁù»\à\çÞ$_\Ú+\ëU\×O\ÈÝ\Ãö\ä\Ë&?Üÿ\0««\áûL\Ïü?þ&©Mgy>¡\ÐÝ!RmcüX7\ÐñKk¡\êD*Í©¾\Íñ³ª\Ê\çx\ÜrNF}©\Â2\Ü\Ò	\ãk§dûOX»r[NQ÷\ÍjÁ\á\Í8q!R2|,W9¯ÀSr±\Äñ¶rÛnõ=õ\â·m4b·P\Ò9|²ª©U\nx98\Î+XÓ§mgøKoDþñ\ÖñiV@µ\Ìû\\\é=~÷J\Ù\Zõ³rÌ¨5Ý\ã#ý\ë./\é1Æ©º\\eO´P0#\à\é\í[6Zv	¢6bI\ÞN\ã\Ù<~kHÆz·øõ*\â*ü_¹-û+$;GTªö \0`v5Jµ\Ær\ÜLQKEQ\"QKE\0-´PVd\Ù\áù8\ê¤z¿Òº\n\å|vGöU¸\'M¼\\)Ö¢£´Yt\Õ\ä21+¶9`HRº\Ã\æ\ÄL®0Hù\Ó8\éþzT\Þ6}\Ø\Ãz²mT	\æ6\àr	=1^|m½\Î\é6\Ö\ÄjEfü(\\ógO5§h\éù×g)\0ýx¬³a.áÕ¿Â´--\í¸·\ÞOË·x$/½¸h­\ßò2w\íù1& ²ó\ãÁ^=;ó\Ó\ÜqúÕ©<ÿ\0>GP0\Ø`¬A\n6\à\Ïð9¬ûx\Ú\ÎWH­Õ°\\`ü~\'¯¥Z\Ð\í\\Ú²\ÚS\0ü­8\ï}?=­¯\âiÚ¥Äö¹ d&9\'#\Çñ\Ç5zo³NPKu rrr=r=+6;wO(Em·O\×\é\ëõ«\ÂØie¨T¼\0\äõ>ü\Õ&½ÿ\0!8·¥¿2Ê¾\ÑE¹\är¨vC\0N\ë\Åni´WM\áI\'\ç<õ\ëXöVu@m\"$V½­$a\Þ_9ù#ð­.µ\Ìù\\]\ìX½¡QÐ*j\\\rö/ \Öemå¸R\ÑTHR\Ñ@E-\0\Âxò\à¸ ÝH÷~$ÿ\0õ«¼¯3ñ¹\í\Ù¾\éUÁ+*\ß¥/\çc	\Î\ï\Ìbµ\"E#ÿ\0\ÕX\Íare+¶¶\à\Ã\èzV½¤F4@@©\í\\u#«¦uBRn\Í\äv\ÒÆ«x\Ãh\Ë\ØsÒ\í­UF/\æØ¸\Âù\ÜqÒ´J\Êr¡»r([;q\Ü\'=~Z\É8\ÛSURkH½³@ ·I1dÂ\àþ?^\rL \ßT\ÕfV`IF;zÿ\0\ÛÄ²oX\Ô7\\þ}MX\ÞQa@\Ïôÿ\0MJ%{J\ßO\ë\äB\"\ÓÃ-\ä\ì7À\È@.\'·\\þu~84m­O\0\ì}=N)û-\Ø\îò¶yb£\'üóWA\Æ\0Òµ¼ÄºµÚ±£¦=¼q·nÐ®\ì\á¸\É\ãr\ëU\Äd¤\ÊT²\Å\nr_\çY\É*(-®SQu\Ô\\\Ã2\ãc\Î06ó;ô­i8·\ïlr\ÖskM[\êu¶ó%\Ô£`UÔC\è}\ë>IXÚ¬BC&2\Ì\Çf<q\îh®µ­¶0ô\æ\ÜJ)hª$J)h ¢Q\0+Vðå¦­0\ÞH¦Û°²7Q\î+cbI«1¦\Ö\Ç%ÿ\0-¸PÀ\Èþ 7\æ4õð^\ÜcQ\Û\Ëº¬Qe\Å{Iw9\Åð¨S\Å\é\Çýr\ãW\"ðõ¢.$gúô­|Q=;´s0\è6»aG4\Õðý¢ñ|Ö®(\Å?g\Â\çs,h6£ø\æ?ð!þ¿\Øvû\Ó\ßu§1G³`\çs=t{U\é\æ~-V\ÊúùÕQ|]\ÎO¨Õ;Tõ§Q1UkQ1@b\0Z(¢\n(¢\n(¢\n(¢\n(¢\n(¢\n(¢\n(¢\n(¢\n(¢?ÿ\Ù','construccion','SACO',NULL,NULL);
/*!40000 ALTER TABLE `producto` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `proveedores`
--

DROP TABLE IF EXISTS `proveedores`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `proveedores` (
  `Marca` varchar(50) NOT NULL,
  `Telefono` varchar(11) DEFAULT NULL,
  `Direccion` varchar(100) DEFAULT NULL,
  `correo` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`Marca`)
) ENGINE=InnoDB;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `proveedores`
--

LOCK TABLES `proveedores` WRITE;
/*!40000 ALTER TABLE `proveedores` DISABLE KEYS */;
INSERT INTO `proveedores` VALUES ('Apasco','3212531523','av. aviacion','hsdbaa'),('ARMASEL','3333684354','ANILLO EPRIFERICO PONIENTE 2320 LOMAS DEL COLI','cperez@armasel.com'),('PROMAPESA','3332525958','SAN JUAN ESCUTIA','DSAS');
/*!40000 ALTER TABLE `proveedores` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `subproducto`
--

DROP TABLE IF EXISTS `subproducto`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `subproducto` (
  `Nombre` varchar(100) NOT NULL,
  `ClaveAlterna` varchar(50) NOT NULL,
  `Precio` decimal(10,2) DEFAULT NULL,
  `Unidad` varchar(30) DEFAULT NULL,
  `Codigo` int DEFAULT NULL,
  `granel` varchar(10) DEFAULT NULL,
  `imagen` longblob,
  PRIMARY KEY (`ClaveAlterna`),
  KEY `Unidad` (`Unidad`),
  KEY `fk_subproducto_producto` (`Codigo`),
  CONSTRAINT `fk_subproducto_producto` FOREIGN KEY (`Codigo`) REFERENCES `producto` (`Codigo`)
) ENGINE=InnoDB;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `subproducto`
--

CREATE TABLE model (
id int NOT NULL auto_increment primary key,
b0 decimal NOT NULL,
b1 decimal NOT NULL,
b2 decimal NOT NULL,
fecha_entrenamiento date NOT NULL,
version VARCHAR(20) DEFAULT 'v1',
comentario TEXT
);

CREATE TABLE predicciones (
id int NOT NULL auto_increment primary key,
anio int NOT NULL,
mes int NOT NULL,
prediccion decimal NOT NULL,
fecha_calculo date NOT NULL, 
version VARCHAR(20) default 'v1'
);

LOCK TABLES `subproducto` WRITE;
/*!40000 ALTER TABLE `subproducto` DISABLE KEYS */;
INSERT INTO `subproducto` VALUES ('kilo de cemento ','kil-cem',5.00,'kg',8,'si',_binary 'ÿ\Øÿ\à\0JFIF\0\0\0\0\0\0ÿ\Û\0C\0		\n\r\Z\Z $.\' \",#(7),01444\'9=82<.342ÿ\Û\0C			\r\r2!!22222222222222222222222222222222222222222222222222ÿÀ\0\0\0\Â\"\0ÿ\Ä\0\0\0\0\0\0\0\0\0\0\0	\nÿ\Ä\0µ\0\0\0}\0!1AQa\"q2¡#B±ÁR\Ñð$3br	\n\Z%&\'()*456789:CDEFGHIJSTUVWXYZcdefghijstuvwxyz¢£¤¥¦§¨©ª²³´µ¶·¸¹º\Â\Ã\Ä\Å\Æ\Ç\È\É\Ê\Ò\Ó\Ô\Õ\Ö\×\Ø\Ù\Ú\á\â\ã\ä\å\æ\ç\è\é\êñòóôõö÷øùúÿ\Ä\0\0\0\0\0\0\0\0	\nÿ\Ä\0µ\0\0w\0!1AQaq\"2B¡±Á	#3Rðbr\Ñ\n$4\á%ñ\Z&\'()*56789:CDEFGHIJSTUVWXYZcdefghijstuvwxyz¢£¤¥¦§¨©ª²³´µ¶·¸¹º\Â\Ã\Ä\Å\Æ\Ç\È\É\Ê\Ò\Ó\Ô\Õ\Ö\×\Ø\Ù\Ú\â\ã\ä\å\æ\ç\è\é\êòóôõö÷øùúÿ\Ú\0\0\0?\0ï¨¥¢Z((¥§*;}\Õ\'\è(S\åá¤J¢òI)\"S<a\âR\Êz(+µ\í \Ú)H+/x\í=qô¢ f\ÅûÀ8%9\Å\Ê\íq(¤!}Ô¶qó{)Az¦\ÑJ=.¡ Ch¥\Å\0R\Ñ@	E-\0R\Ñ@	E-\0R\Ñ@E.(\Å\0%;	½AùÀ88ö¥\År~-\Ô/4\ë+\Ë9J¬®duü\ê*6£t\\r³:ËX!Ê\ä\ã\æüi`¾W\îl1£=»qYºlµ\ÄS\Ç\å\Üc¾µ¶mcl¼G(°\Ñê®«FF/\Ê\à{Ò²º\Å\æ6+g<sÿ\0ê¦´BF\nV³n!góÔQF~¬\Ûh´6V\àtÁ\Çò©\ã\Ý^\\-X	zñ\à0\"µl\î\ãcw4\Ô\ÛN\Ö\èe\ÛOr% ¶$Só\åW¯\à*ù¿£+\ípx\äTóY«±\0¹lw¬}B\æ\ÛMU{©DjÍ´n\ïG4Q\Æ\î\ÑZb¹[tÚ¡7]¸\Ç\åS%Ô¸TÝ\08ª)²XþCyr\rKfÛ\È\Î\Ó\Æ*y\ßpq]M\äaUB\êOC\È\Ê\å:1¤u7\0Us¾ä¸1f`\È£ó¬ø\ï	6£p?Â¢¸º{V<:\Ñmn@I\êy5.r\èÃ[TlZ\Ã\çD{\ä\n.m`	mrJõ©¾T[S\æ8\â[7A$\Ä\íþ\ï­ivôD\èµ3d£6`~lóøTU©¬25#\å\àv]1M+3N\ïA(¥\ÅªR\Ñ@)qF(1\\¿­ü\Í6	»$OÐþµu8ª\ZÍö=¸û\År¿QÈ©º±Qvw<\rÛºòWc\á\ï\Z-uùz	Æ¹³²L\Ðsõ¨	p·g¡\Ýd\Õ\Ù\îðOÊ®2ðA¦Á\\c\"¼«@ñ=ÆV	¼\0ü ÿ\0\rzU¥\rüH\\â÷!\Æ\Â\\Z§\Ï1\É\îRG½m4 cx\ëX>$\×,´kSÈ\é\Ç\É?©ôt\éJ¬\Ô ®È©Z4 \çQ\Ù¿ñµ¶n\å|Ë~H\à\êOa\\¶¥\â+Y\Ëwg{»¶\É\×\n=\Ë\\.©-\Õ\î ÷s\ÈZIO-ý>\Ö6¤i\Í\Z^_Ý©dmPE{O	\ÃAF½\Û~¿¡\æR\Å\ã+Ts\Â?4´¯i\Ú^\ÛeôÀ[#+lúcw¨·\Ò,°\Éó+^k®øut»[[\è.þ\Ñkq÷I]¤dd}*\â[½\n\äó-³\ÞBO\Üz\Zur\Ê5i{L;1gZ\Ý<J×¯\ëÏúû\Ö\\²=Ì»sÀ¨\ì5[mn\Ýnm%ÞC\Ã!ô\"´m\íUIr1ùù\ÂJ\\¯K\ì%e¨\ëxBG\áª\ÔQa\ç\ÔR*B~«efQ7°\Ë\ÕQB°\ë{\\XcÚ­\\Iö[gp¹\Ú8 ²`\âª\ê\Ò¡¢\Ã75\Ñ©+3;bI=I¤¥\Å­\ÌD¢b\\Q@1K1@	1K1@}\ãm%,\çKø\Ì\Û]}®k[S\"ôê§­z\'[\ÃûO\Þ2®\ßÖ¼\Ú$\Æ\Þ1\Ð\×t´;hÉ¸Ym¥aúV­^\è÷*\ÛY¢\Î}©a¸rG\Ón\ç\Â\ïs\Â/©ÿ\0\n\Çs¢1\æ|¨ô\r[\ÄKm\á¶Ô­\Õdb«°7\08\Éü\ë\É\î®g¼®f¤\ÎY\ëZ²^Os\à­Ï¸\É\Éÿ\0h\Z\å\í\î_g}6L ©\É\Û[7Ójò_D_YU\×cpÇ¦kQüG«O´\ÖTùs\ä¯øV8qmoZô»ùm´\í/N¸M°w1¼\É\0<\í5Ñ\â)R\åö\æ1\Êðu\ë\Ôp£.Vp2k:\ÄÀjRLª~PÇúó·k\È\ÇøcRO\å]\Ú\ÒJ\nÿ\0eiªýmþµr\ê[£h¢\ã.01\\8#\Ëh{ñ5\'\Ï9«x?\Ã\×Z,Í«\ê\Ò8H3a>£úu®\×MÕ­õtajH*p\è\ÝEy\Å\Õ\å\ÅÛg½\\\çkI¹O¹[l ÷\èG¡¯zò>\Ãñ¡q¯/Àõ\Û+U_\ã>õ¢F¬#ST³Y¢\\0\á\Ó9*kU1\éDmm¤%	8\ÍY¢ecÞ¨j\ãýûÒ¯.µg\ê\íòD¹\êI\Åk\r\Ì%±1K1[	1K1@	)h ¢\0J)h ?\Çs\Ò>öPø\×\0À­\ë\×\ë]÷\Ô\ìØòÁ\ìÁ\ÈÀ\"¸±\ß;p\ë\Ý,[\ÊÈ nùI\èy\ÅU¼\Ó\Ì\Ì÷S^¢¨0~t­<p¦\çl\Æ@¨e¹\êÝ w«p®9\íÚ²\Öý¨Ê¤=\êkS[Iµ²ºÑ®-\"º\Å!ù\ÙÒ¹Ç¯Ò¢Ó¼= ^\È\ÑÁy4²¬üv£°xã°\ÚI§;\Ô(rN\å0>_NõgO±\Ó\ìo 	g\rW;s\Üm\ï\í]tªºJJ2k·ü½\Z¦¥8_¾·ü?%\ãYý¦\àÜ¢\î1\ïÇ¯\Ý÷­­WLM*\Þ\ï\ìðZe\ÝÀö¬s\r¥Æ¨5\â\Çò\Ü\ÝNsZ\×W¶÷\Öi7/\È\ÛX\åGôúþU5ªº\nrmuò\îi¥W\Ü\éF\Ò\é§\ÜfYhºmô\âmi$Hö\É\æ¬YhºeýÈ\ßXó$\É-»c#\ß8­\r\"=\'N\É \Ø%`]ú\ãùTze¦^}¶¦\Þ\Ä)RF\Ø\Ï\ähq¡yY½6\Ø\ìYg\î\Ýz\éÿ\0\0§&¥Áq4\ÚN\Ín	ÙÁ98ö\ÍlK´\Óc¿Uuµ)WòOÍi.4\Ý\æK©%W{.N\É2§¶^\É\ÍZû&{[{)\Ó¨\0\É\â­\Ç\r\îÙ¿=¿­\ÈX\ì\Ùó~\Z\Ò\í\âò\ï\ì5&A\Óo=\Çc]D[ðw°\'<`c\ãt«\í;I·h¬ð¹.C 1ô\Ý\Û\ÕX^\Û6$g$`þY¬\ß\"mCc¯\Ö&\ë¯{©|\n\Ê\ÕuÊÁ^i&@>D9\í¿Ì¼}÷,pF00kJ{³Ø¯E-±R\Ñ@	E-\0´R\Ñ@	E-\Íø\Õc:2ûþh	õ \çô\ås\Ê\ÑM\Z­Y\Î?\ï^»\â6mKGe¨&\0\Ç\0\àóÛ^Ot\ì$p}Cps\\Õ¥vº\Z\ÆÉü±cB\Å\ÙF\Ö\á­HöVòùrAq½j;qn\Îs\È<¨\Î}jy!³»¾g\\[*×+5\ì\ßsG*Z2\Üo¤òÙ¬÷\ä\r§oSWc:1\ÝóYezü\Ë\Ç\×Ò³Mü§\ÊÚ²¤\Å]G³\Ûg\ÌT\É\ç+#r\Ô\Z_»\î\Ç\ík-\âk\ÚZ\éW(L1\ÛH\ÞÙ\Ç\åV\â·\Ñü\Ç@¶\Ðe#*=H\íU4\Í:+	gh\Ù\ÚI\Èw\ÞÀE>\ß\Ã\Ö-\ÄA¥6÷gx\Æ\ìõ\ê=jR3¼ö\ÕyWSACx^U6\r\ZcsnR\Ôö§\Ä\Ú\É%Oh£ ;)í¤<?¥\É\ç¬óI#2,r3HU\È qr_C\Ó\äv \ÜW\Ëð\ÛG\Ê\0­\\i\ßw÷¬F#úlÐ\Æ\ë\Û\"\Íw\Ú:ócV\ÅÖ^i+û\Æzz\Õ+-LbÄ¹\Û[\æ±\Ê7$u«³¾û#¹9(w¡?.zg¸\âµJ\Ñ\'øÊ®\'[\É~%U\Ö\ìtû)¦\çÊ\Ë$Eò\î\Ès\ëú{\Õû·®\É>À ©©\Z\ëOa!·\ÉpÞ±/Î¹[ÓÖ´,n­\ä\Ê@\0\08ª³¿+k\Ì\Ên¬ô\çIi÷wI\ç\Úù1û²I\É\äxô\0þ5RbZy	\êX\Ö\Äm\È=¬G\ïý\ãUm+I&\îGE-¡QKE\0%´P\ÑKE\0%´Pg&[}öW\áDD~|Zò²²3\Ô×ªøºTM	\â|fVU\0÷\ç?Ò¼ËÄ\ç5Çz¤u\áôW+Á´ð¾S\ÎcùÓ\Ø\îr\ÖÉ´Ào\ëù\äV\Ò5`s\ê*o±\ÄûU\Ð\ÛqF\Ò\æeÖ²;yðLñ¸ó\é\Æsô\ë\Ó46\Úc4I*Tª·l\è\r>\â\ÎX¼ÇP¹\0yg9?JKI­ùû\0\0\ß)\à{ö\äU).\äû9\É]Gð4\Úv¿?0~Q\Êñ\éÇ½_K{ctJñDwp9ùI\Ïls\ÇZ«yC\Æo \ÞwT`÷úÕ·\ì.ºBIU] `~\ëù\Ó\ç\é\Íý}\Äû)-\ãý}\ä·-§¤³<©.\ã \Þ\ë\êªs(ö«ñX\r\ß\èÓ°´29\ê?xÿ\0MhU¶¥r£Ö¤[\ëX\â\Øu,§¹=ýª\çIY?Å\ÙNNü¿«löðÄÁ¼ùI\Ïç°¥K\Ú|­>R_|À@\É\Æs\×\ÔþF³a½\Ó\Ûz5õË$½ü=OO~µi\ï´ù\Â\êq!\nvFý#9Ç¹¦¥ûÐ¨ú[\äk\Åw1Ú±Ùªü¸Áa×Ê¬Y^,L\ßh0\Ä\ÈXþ§:\äcõª\ÚtS:;sgH\'CÚ¶!(°¨\ÉV¥¢F¢õÜ¹nþaVUý\Ù\êO¥S¼e\Ó\ã8\'#\"® \\dTz\ßr}TÖ\ßC9ôR\ÑZE-\0R\Ñ@E-\0R\Ñ@?®3q ÿ\0«±\äÿ\0õ«\àB\Ùbª\àg5\Ùø\æ\Ö_\íh\ÝC,` \í\È\'¿\å\\ºYxû\ÞYùO\àÂ¸\ê[\Ú{\ÇT/\Éî±\×J\Ò\0>a\Òõ«V0¦}Ç¥g[\ØÁl\ÄbRÁx\ÊdÔ.\ì_Á«\Zª7÷64½\àa\"\ìò£ß\Ãq\ëQ¬\Û.Ç\È\0÷ ~?h 1R,Gsô¨M%ª+ôv3«2\î°b \'g ñÀ\È÷ý\r]\âxdO±²FqC½±\Üñüª\ÊÁ)8±ú\nvÂ V\0uÊ(¿÷JRYHN¨Ç¿\èjs­ôôh¶¬¸ûÄ=\èH\Ø\r²)Æ¬%Ò7¯LqZFJú\ÄNS·\Ä>\Ö\êñ°\í;AÁx\Ïn*¹÷À<ü\Äp=?\Z¨J?\å§_zoc;³ô§~Èw»5\ínfTýóI\ì:JuÞµ®\ÈDµÜ¼C>ð¬ô½R8SD\Öö\á~\Ñl²2ô\'?\éZRk\ß\ØÎª/¹¿£©wk¨,WrA(¸eX¢L\ç$\äò>¿0\Ýg ô!`ÁQ2J«½T\"º \ÜaJ\ÓÐÁ]\Ýs]\nJMYr¸§vCE-¡QKE\0%´P\ÑKE\0%´PE*ê¬§¨aj±\Ò\ìl\à\Éô\n¹E&¿²¬?\ç\Ò/Êi\Zv\í\ßbR\Õ\ê(\å]vSþË°ÿ\08?\ïR\Çio	\ÌpD\ÕP\n,q(¥¢	F)h ¢\0J)h ¢\0J)h ¢\0J)h ÿ\Ù');
/*!40000 ALTER TABLE `subproducto` ENABLE KEYS */;
UNLOCK TABLES;


--
-- Table structure for table `trabajadores`
--

DROP TABLE IF EXISTS `trabajadores`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `trabajadores` (
  `Usuario_trabajador` varchar(30) NOT NULL,
  `Rol` varchar(30) DEFAULT NULL,
  PRIMARY KEY (`Usuario_trabajador`)
) ENGINE=InnoDB;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `trabajadores`
--

LOCK TABLES `trabajadores` WRITE;
/*!40000 ALTER TABLE `trabajadores` DISABLE KEYS */;
INSERT INTO `trabajadores` VALUES ('asd','repartidor'),('marco','vendedor'),('niÃ±ote','repartidor');
/*!40000 ALTER TABLE `trabajadores` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `unidad`
--

DROP TABLE IF EXISTS `unidad`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `unidad` (
  `Unidad` varchar(30) NOT NULL,
  `Formato` decimal(10,0) DEFAULT NULL,
  PRIMARY KEY (`Unidad`)
) ENGINE=InnoDB;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `unidad`
--

LOCK TABLES `unidad` WRITE;
/*!40000 ALTER TABLE `unidad` DISABLE KEYS */;
INSERT INTO `unidad` VALUES ('kg',1),('metro',1),('PIEZA',1),('SACO',25),('saco 50kl',50),('tramo',6),('tramo3',3),('tramo4',4);
/*!40000 ALTER TABLE `unidad` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `usuario`
--

DROP TABLE IF EXISTS `usuario`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `usuario` (
  `Usuario` varchar(30) NOT NULL,
  `ContraseÃ±a` varchar(16) DEFAULT NULL,
  `Rol` varchar(30) DEFAULT NULL,
  PRIMARY KEY (`Usuario`)
) ENGINE=InnoDB;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `usuario`
--

LOCK TABLES `usuario` WRITE;
/*!40000 ALTER TABLE `usuario` DISABLE KEYS */;
INSERT INTO `usuario` VALUES ('admin','123','amdin');
/*!40000 ALTER TABLE `usuario` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `venta`
--

DROP TABLE IF EXISTS `venta`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `venta` (
  `Id_Ventas` int NOT NULL AUTO_INCREMENT,
  `Id_Clientes` int DEFAULT NULL,
  `Fecha` varchar(100) DEFAULT NULL,
  `Total` decimal(10,0) DEFAULT NULL,
  `Id_Vendedor` varchar(30) DEFAULT NULL,
  `Id_Repartidor` varchar(30) DEFAULT NULL,
  `Status` varchar(30) DEFAULT NULL,
  PRIMARY KEY (`Id_Ventas`),
  KEY `Id_Clientes` (`Id_Clientes`),
  KEY `Id_Vendedor` (`Id_Vendedor`),
  KEY `Id_Repartidor` (`Id_Repartidor`)
) ENGINE=InnoDB AUTO_INCREMENT=146;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `venta`
--

LOCK TABLES `venta` WRITE;
/*!40000 ALTER TABLE `venta` DISABLE KEYS */;
INSERT INTO `venta` VALUES (142,2,'10/12/2023',230,'marco','Sin Repartidor','enviado'),(143,0,'12/12/2023',800,'marco','Sin Repartidor','Venta'),(144,0,'18/01/2024',5,'marco','Sin Repartidor','devolucion'),(145,2,'18/01/2024',175,'marco','Sin Repartidor','enviado');
/*!40000 ALTER TABLE `venta` ENABLE KEYS */;
UNLOCK TABLES;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50017 DEFINER=`root`@`localhost`*/ /*!50003 TRIGGER `actualizar_inventario` AFTER UPDATE ON `venta` FOR EACH ROW BEGIN
    DECLARE equivalencia INT DEFAULT 1;
    DECLARE codigo_principal VARCHAR(30);
    DECLARE cantidad_producto DECIMAL(10,2);
    DECLARE unidad_producto VARCHAR(30);
    DECLARE existencia_actual DECIMAL(10,2) DEFAULT 0;
    DECLARE done INT DEFAULT FALSE;

    DECLARE cursor_productos CURSOR FOR
        SELECT Codigo, Cantidad, Unidad
        FROM detalle_venta
        WHERE Id_Ventas = NEW.Id_Ventas;

    DECLARE CONTINUE HANDLER FOR NOT FOUND SET done = TRUE;

    IF NEW.Status = 'Venta' THEN
        OPEN cursor_productos;
        loop_productos: LOOP
            FETCH cursor_productos INTO codigo_principal, cantidad_producto, unidad_producto;
            IF done THEN
                LEAVE loop_productos;
            END IF;

            -- Buscar la equivalencia en la tabla unidad
            SELECT COALESCE(Formato, 1) INTO equivalencia
            FROM unidad WHERE Unidad = unidad_producto;

            -- Intentar encontrar el producto o subproducto y su existencia
            SELECT p.Codigo, p.Existencia INTO codigo_principal, existencia_actual
            FROM producto p
            LEFT JOIN subproducto s ON s.Codigo = p.Codigo
            WHERE p.Codigo = codigo_principal OR s.ClaveAlterna = codigo_principal;

            -- Actualizar la existencia del producto principal
            IF existencia_actual IS NOT NULL THEN
                UPDATE producto
                SET Existencia = COALESCE(existencia_actual - (cantidad_producto * equivalencia), 0)
                WHERE Codigo = codigo_principal;
            END IF;
        END LOOP;
        CLOSE cursor_productos;
    END IF;
END */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50017 DEFINER=`root`@`localhost`*/ /*!50003 TRIGGER `actualizar_devolucion` AFTER UPDATE ON `venta` FOR EACH ROW BEGIN
    DECLARE equivalencia INT DEFAULT 1;
    DECLARE codigo_principal VARCHAR(30);
    DECLARE cantidad_producto DECIMAL(10,2);
    DECLARE unidad_producto VARCHAR(30);
    DECLARE existencia_actual DECIMAL(10,2) DEFAULT 0;
    DECLARE done INT DEFAULT FALSE;

    DECLARE cursor_productos CURSOR FOR
        SELECT Codigo, Cantidad, Unidad
        FROM detalle_venta
        WHERE Id_Ventas = NEW.Id_Ventas;

    DECLARE CONTINUE HANDLER FOR NOT FOUND SET done = TRUE;

    IF NEW.Status = 'devolucion' THEN
        OPEN cursor_productos;
        loop_productos: LOOP
            FETCH cursor_productos INTO codigo_principal, cantidad_producto, unidad_producto;
            IF done THEN
                LEAVE loop_productos;
            END IF;

            -- Buscar la equivalencia en la tabla unidad
            SELECT COALESCE(Formato, 1) INTO equivalencia
            FROM unidad WHERE Unidad = unidad_producto;

            -- Intentar encontrar el producto o subproducto y su existencia
            SELECT p.Codigo, p.Existencia INTO codigo_principal, existencia_actual
            FROM producto p
            LEFT JOIN subproducto s ON s.Codigo = p.Codigo
            WHERE p.Codigo = codigo_principal OR s.ClaveAlterna = codigo_principal;

            -- Actualizar la existencia del producto principal
            IF existencia_actual IS NOT NULL THEN
                UPDATE producto
                SET Existencia = COALESCE(existencia_actual + (cantidad_producto * equivalencia), 0)
                WHERE Codigo = codigo_principal;
            END IF;
        END LOOP;
        CLOSE cursor_productos;
    END IF;
END */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;

--
-- Dumping events for database 'tlapaleria'
--

--
-- Dumping routines for database 'tlapaleria'
--
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2024-01-22 22:24:27
