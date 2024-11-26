-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Servidor: 127.0.0.1
-- Tiempo de generación: 22-10-2024 a las 19:23:03
-- Versión del servidor: 10.4.32-MariaDB
-- Versión de PHP: 8.2.12

-- Crear base de datos
CREATE DATABASE IF NOT EXISTS bd_prueba;
USE bd_prueba;

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Base de datos: `bd_prueba`
--

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `cliente`
--

-- Crear tabla Cliente
CREATE TABLE Cliente (
  id_cliente INT AUTO_INCREMENT PRIMARY KEY,
  nombre_cliente VARCHAR(100) NOT NULL,
  nif VARCHAR(20) NOT NULL UNIQUE,
  email VARCHAR(100) NOT NULL UNIQUE,
  telefono VARCHAR(20)
);

-- Crear tabla Producto
CREATE TABLE Producto (
  id_producto INT AUTO_INCREMENT PRIMARY KEY,
  nombre_producto VARCHAR(100) NOT NULL,
  descripcion TEXT,
  precio_unitario DECIMAL(10, 2) NOT NULL,
  categoria VARCHAR(50)
);

-- Crear tabla Pedido
CREATE TABLE Pedido (
  id_pedido INT AUTO_INCREMENT PRIMARY KEY,
  id_cliente INT NOT NULL,
  fecha_pedido DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  total_pedido DECIMAL(10, 2),
  estado_pedido VARCHAR(50),
  FOREIGN KEY (id_cliente) REFERENCES Cliente(id_cliente)
);

-- Crear tabla Detalle_Pedido (relación entre Pedido y Producto)
CREATE TABLE Detalle_Pedido (
  id_detalle_pedido INT AUTO_INCREMENT PRIMARY KEY,
  id_pedido INT NOT NULL,
  id_producto INT NOT NULL,
  cantidad INT NOT NULL,
  precio_total DECIMAL(10, 2) NOT NULL,
  FOREIGN KEY (id_pedido) REFERENCES Pedido(id_pedido),
  FOREIGN KEY (id_producto) REFERENCES Producto(id_producto)
);

--
-- Volcado de datos para la tabla `producto`
--
INSERT INTO Producto (nombre_producto, descripcion, precio_unitario, categoria)
VALUES ('Bruschetta', 'Pan tostado con tomate fresco, albahaca, ajo y aceite de oliva', 5.00, 'Entrante');

INSERT INTO Producto (nombre_producto, descripcion, precio_unitario, categoria)
VALUES ('Gazpacho', 'Sopa fría de tomate, pimiento, pepino, ajo y aceite de oliva', 4.50, 'Entrante');

INSERT INTO Producto (nombre_producto, descripcion, precio_unitario, categoria)
VALUES ('Hummus', 'Puré de garbanzos, tahini, ajo, jugo de limón y aceite de oliva', 3.50, 'Entrante');
INSERT INTO Producto (nombre_producto, descripcion, precio_unitario, categoria)
VALUES ('Paella de Mariscos', 'Arroz con camarones, mejillones, calamares, pimiento y azafrán', 15.00, 'Plato');

INSERT INTO Producto (nombre_producto, descripcion, precio_unitario, categoria)
VALUES ('Pollo al Curry', 'Pollo en salsa de curry, leche de coco, cebolla, ajo y jengibre', 12.50, 'Plato');

INSERT INTO Producto (nombre_producto, descripcion, precio_unitario, categoria)
VALUES ('Lasagna de Carne', 'Capas de pasta con carne molida, salsa de tomate, bechamel y queso', 13.00, 'Plato');
INSERT INTO Producto (nombre_producto, descripcion, precio_unitario, categoria)
VALUES ('Coca-Cola', 'Refresco carbonatado de cola', 2.00, 'Bebida');

INSERT INTO Producto (nombre_producto, descripcion, precio_unitario, categoria)
VALUES ('Limonada', 'Bebida refrescante de limón', 2.50, 'Bebida');

INSERT INTO Producto (nombre_producto, descripcion, precio_unitario, categoria)
VALUES ('Café Espresso', 'Café concentrado de sabor fuerte y aroma intenso', 1.80, 'Bebida');
INSERT INTO Producto (nombre_producto, descripcion, precio_unitario, categoria)
VALUES ('Tarta de Queso', 'Queso crema, azúcar, galleta triturada y un toque de vainilla', 4.50, 'Postre');

INSERT INTO Producto (nombre_producto, descripcion, precio_unitario, categoria)
VALUES ('Tiramisu', 'Bizcochos, café, crema de mascarpone y cacao en polvo', 5.00, 'Postre');

INSERT INTO Producto (nombre_producto, descripcion, precio_unitario, categoria)
VALUES ('Helado de Vainilla', 'Leche, crema, azúcar y extracto natural de vainilla', 3.00, 'Postre');
INSERT INTO Producto (nombre_producto, descripcion, precio_unitario, categoria)
VALUES ('Calamares a la Romana', 'Calamares rebozados en harina y fritos, acompañados con limón', 6.00, 'Entrante');

INSERT INTO Producto (nombre_producto, descripcion, precio_unitario, categoria)
VALUES ('Croquetas de Jamón', 'Croquetas caseras de jamón ibérico, bechamel y pan rallado', 5.50, 'Entrante');

INSERT INTO Producto (nombre_producto, descripcion, precio_unitario, categoria)
VALUES ('Ensalada Caprese', 'Mozzarella fresca, tomates, albahaca y un toque de aceite de oliva', 4.00, 'Entrante');

INSERT INTO Producto (nombre_producto, descripcion, precio_unitario, categoria)
VALUES ('Tartar de Atún', 'Atún fresco, aguacate, cebolla, salsa de soja y un toque de lima', 8.00, 'Entrante');
INSERT INTO Producto (nombre_producto, descripcion, precio_unitario, categoria)
VALUES ('Entrecot a la Parrilla', 'Entrecot de ternera a la parrilla con sal gruesa y hierbas frescas', 18.00, 'Plato');

INSERT INTO Producto (nombre_producto, descripcion, precio_unitario, categoria)
VALUES ('Raviolis de Espinacas', 'Raviolis rellenos de espinacas y ricotta en salsa de tomate', 12.00, 'Plato');

INSERT INTO Producto (nombre_producto, descripcion, precio_unitario, categoria)
VALUES ('Risotto de Setas', 'Arroz cremoso con setas, queso parmesano y un toque de trufa', 13.50, 'Plato');

INSERT INTO Producto (nombre_producto, descripcion, precio_unitario, categoria)
VALUES ('Bacalao a la Vizcaína', 'Lomo de bacalao en salsa de tomate, pimientos y cebolla', 14.50, 'Plato');
INSERT INTO Producto (nombre_producto, descripcion, precio_unitario, categoria)
VALUES ('Té Verde', 'Té verde natural, refrescante y antioxidante', 2.00, 'Bebida');

INSERT INTO Producto (nombre_producto, descripcion, precio_unitario, categoria)
VALUES ('Jugo de Naranja', 'Zumo de naranjas frescas, sin azúcar añadido', 2.50, 'Bebida');

INSERT INTO Producto (nombre_producto, descripcion, precio_unitario, categoria)
VALUES ('Batido de Fresa', 'Batido cremoso de fresas naturales y leche', 3.00, 'Bebida');

INSERT INTO Producto (nombre_producto, descripcion, precio_unitario, categoria)
VALUES ('Cerveza Artesanal', 'Cerveza rubia artesanal de cuerpo suave y sabor afrutado', 3.50, 'Bebida');
INSERT INTO Producto (nombre_producto, descripcion, precio_unitario, categoria)
VALUES ('Brownie de Chocolate', 'Brownie de chocolate con nueces y un toque de vainilla', 4.00, 'Postre');

INSERT INTO Producto (nombre_producto, descripcion, precio_unitario, categoria)
VALUES ('Crema Catalana', 'Postre cremoso de yema de huevo y azúcar caramelizado', 4.50, 'Postre');

INSERT INTO Producto (nombre_producto, descripcion, precio_unitario, categoria)
VALUES ('Flan de Caramelo', 'Flan casero de caramelo, leche y huevo', 3.50, 'Postre');

INSERT INTO Producto (nombre_producto, descripcion, precio_unitario, categoria)
VALUES ('Tarta de Manzana', 'Tarta de manzana caramelizada con base de hojaldre', 5.00, 'Postre');
INSERT INTO Producto (nombre_producto, descripcion, precio_unitario, categoria)
VALUES ('Bruschetta', 'Pan tostado con tomate fresco, albahaca, ajo y aceite de oliva', 4.00, 'Entrante');

INSERT INTO Producto (nombre_producto, descripcion, precio_unitario, categoria)
VALUES ('Nachos con Guacamole', 'Totopos de maíz con guacamole casero, pico de gallo y queso fundido', 5.50, 'Entrante');

INSERT INTO Producto (nombre_producto, descripcion, precio_unitario, categoria)
VALUES ('Patatas Bravas', 'Patatas fritas acompañadas con salsa brava picante y alioli', 4.50, 'Entrante');

INSERT INTO Producto (nombre_producto, descripcion, precio_unitario, categoria)
VALUES ('Queso Provolone al Horno', 'Provolone gratinado con hierbas, ajo y un toque de pimienta', 6.50, 'Entrante');
INSERT INTO Producto (nombre_producto, descripcion, precio_unitario, categoria)
VALUES ('Pollo al Curry', 'Pollo troceado en una cremosa salsa de curry con arroz basmati', 11.50, 'Plato');

INSERT INTO Producto (nombre_producto, descripcion, precio_unitario, categoria)
VALUES ('Paella de Mariscos', 'Arroz con mejillones, gambas, calamares y un toque de azafrán', 15.00, 'Plato');

INSERT INTO Producto (nombre_producto, descripcion, precio_unitario, categoria)
VALUES ('Lasagna Boloñesa', 'Pasta al horno con salsa boloñesa, bechamel y queso gratinado', 12.50, 'Plato');

INSERT INTO Producto (nombre_producto, descripcion, precio_unitario, categoria)
VALUES ('Hamburguesa Gourmet', 'Hamburguesa de ternera con queso cheddar, bacon, lechuga y tomate', 10.00, 'Plato');
INSERT INTO Producto (nombre_producto, descripcion, precio_unitario, categoria)
VALUES ('Limonada Casera', 'Refrescante limonada con un toque de menta', 2.00, 'Bebida');

INSERT INTO Producto (nombre_producto, descripcion, precio_unitario, categoria)
VALUES ('Agua Mineral', 'Agua mineral natural, sin gas', 1.00, 'Bebida');

INSERT INTO Producto (nombre_producto, descripcion, precio_unitario, categoria)
VALUES ('Mojito Sin Alcohol', 'Bebida de menta, lima y soda, sin alcohol', 3.50, 'Bebida');

INSERT INTO Producto (nombre_producto, descripcion, precio_unitario, categoria)
VALUES ('Café Espresso', 'Café espresso intenso y aromático', 1.50, 'Bebida');
INSERT INTO Producto (nombre_producto, descripcion, precio_unitario, categoria)
VALUES ('Cheesecake', 'Tarta de queso cremosa con base de galleta y cobertura de frutos rojos', 4.50, 'Postre');

INSERT INTO Producto (nombre_producto, descripcion, precio_unitario, categoria)
VALUES ('Panna Cotta', 'Postre italiano de nata, vainilla y azúcar con coulis de frambuesa', 4.00, 'Postre');

INSERT INTO Producto (nombre_producto, descripcion, precio_unitario, categoria)
VALUES ('Tiramisú', 'Postre italiano de bizcocho de café, mascarpone y cacao en polvo', 4.50, 'Postre');

INSERT INTO Producto (nombre_producto, descripcion, precio_unitario, categoria)
VALUES ('Helado Artesanal', 'Helado casero de vainilla, chocolate o fresa', 3.00, 'Postre');
INSERT INTO Producto (nombre_producto, descripcion, precio_unitario, categoria)
VALUES ('Calamares a la Romana', 'Calamares frescos rebozados en harina y fritos hasta dorarse', 7.00, 'Entrante');

INSERT INTO Producto (nombre_producto, descripcion, precio_unitario, categoria)
VALUES ('Empanadillas de Carne', 'Empanadillas rellenas de carne, cebolla y especias, fritas al momento', 5.50, 'Entrante');

INSERT INTO Producto (nombre_producto, descripcion, precio_unitario, categoria)
VALUES ('Tartar de Salmón', 'Salmón fresco marinado con aguacate, cebolla y un toque de limón', 8.00, 'Entrante');

INSERT INTO Producto (nombre_producto, descripcion, precio_unitario, categoria)
VALUES ('Hummus con Pan de Pita', 'Hummus casero con garbanzos, tahini, ajo y aceite de oliva, servido con pan de pita', 4.50, 'Entrante');
INSERT INTO Producto (nombre_producto, descripcion, precio_unitario, categoria)
VALUES ('Raviolis de Espinaca y Ricotta', 'Pasta rellena de espinacas y queso ricotta, en salsa de tomate y albahaca', 10.50, 'Plato');

INSERT INTO Producto (nombre_producto, descripcion, precio_unitario, categoria)
VALUES ('Bacalao a la Vizcaína', 'Lomo de bacalao en salsa de pimientos rojos, cebolla y tomate', 13.00, 'Plato');

INSERT INTO Producto (nombre_producto, descripcion, precio_unitario, categoria)
VALUES ('Cochinita Pibil', 'Cerdo marinado en achiote y especias, acompañado de tortillas de maíz y salsa de habanero', 11.50, 'Plato');

INSERT INTO Producto (nombre_producto, descripcion, precio_unitario, categoria)
VALUES ('Risotto de Setas', 'Arroz cremoso cocinado con setas frescas, queso parmesano y un toque de trufa', 12.00, 'Plato');
INSERT INTO Producto (nombre_producto, descripcion, precio_unitario, categoria)
VALUES ('Zumo de Naranja Natural', 'Zumo recién exprimido de naranjas frescas', 2.50, 'Bebida');

INSERT INTO Producto (nombre_producto, descripcion, precio_unitario, categoria)
VALUES ('Té Helado con Limón', 'Bebida refrescante de té negro frío con rodajas de limón', 2.00, 'Bebida');

INSERT INTO Producto (nombre_producto, descripcion, precio_unitario, categoria)
VALUES ('Batido de Fresa', 'Batido cremoso de fresas frescas con un toque de leche y azúcar', 3.00, 'Bebida');

INSERT INTO Producto (nombre_producto, descripcion, precio_unitario, categoria)
VALUES ('Cerveza Artesanal', 'Cerveza rubia artesanal, con notas afrutadas y ligeramente amarga', 3.50, 'Bebida');
INSERT INTO Producto (nombre_producto, descripcion, precio_unitario, categoria)
VALUES ('Brownie de Chocolate', 'Brownie casero de chocolate con nueces, servido caliente con helado de vainilla', 4.00, 'Postre');

INSERT INTO Producto (nombre_producto, descripcion, precio_unitario, categoria)
VALUES ('Crème Brûlée', 'Postre francés con crema de vainilla y una capa crujiente de azúcar caramelizado', 4.50, 'Postre');

INSERT INTO Producto (nombre_producto, descripcion, precio_unitario, categoria)
VALUES ('Flan de Coco', 'Flan casero con leche de coco, cubierto con caramelo líquido', 3.50, 'Postre');

INSERT INTO Producto (nombre_producto, descripcion, precio_unitario, categoria)
VALUES ('Tarta de Zanahoria', 'Tarta de zanahoria con nueces y crema de queso, suave y esponjosa', 4.00, 'Postre');

--
-- Índices para tablas volcadas
--

--
-- Indices de la tabla `cliente`
--

COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
