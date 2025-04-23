import pandas as pd

# Leer el CSV
df = pd.read_csv("ventas_tlapaleria.csv")

# Preparar variables
X1 = df["anio"].tolist()
X2 = df["mes"].tolist()
Y = df["ventas"].tolist()

n = len(Y)

# Calcular medias
mean_X1 = sum(X1) / n
mean_X2 = sum(X2) / n
mean_Y = sum(Y) / n

# Calcular coeficientes para regresión lineal múltiple
# Fórmulas derivadas de mínimos cuadrados (sin librerías)

# Inicializar sumatorias
numerador_b1 = 0
numerador_b2 = 0
denominador_b1 = 0
denominador_b2 = 0
denominador_b12 = 0

for i in range(n):
    x1_i = X1[i] - mean_X1
    x2_i = X2[i] - mean_X2
    y_i = Y[i] - mean_Y

    numerador_b1 += x1_i * y_i
    numerador_b2 += x2_i * y_i
    denominador_b1 += x1_i ** 2
    denominador_b2 += x2_i ** 2
    denominador_b12 += x1_i * x2_i

# Resolver sistema para obtener b1 y b2 ajustados por colinealidad (método simplificado)
# Usamos sistema de ecuaciones normal para 2 variables

# Determinantes
D = denominador_b1 * denominador_b2 - denominador_b12 ** 2
D1 = numerador_b1 * denominador_b2 - numerador_b2 * denominador_b12
D2 = denominador_b1 * numerador_b2 - numerador_b1 * denominador_b12

# Coeficientes
b1 = D1 / D
b2 = D2 / D
b0 = mean_Y - b1 * mean_X1 - b2 * mean_X2

# Mostrar el modelo
print(f"Modelo de regresión:")
print(f"ventas = {b0:.2f} + ({b1:.2f} * año) + ({b2:.2f} * mes)")

# Función de predicción
def predecir_ventas(anio, mes):
    return round(b0 + b1 * anio + b2 * mes, 2)

# Ejemplo: predecir para julio 2025
print("Predicción para julio 2025:", predecir_ventas(2025, 7))
