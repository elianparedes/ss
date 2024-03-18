import csv
import matplotlib.pyplot as plt

# Leer datos del CSV
N = []
M = []
with open('../../output/analytics.csv', 'r') as file:
    reader = csv.DictReader(file, delimiter=',')
    for row in reader:
        N.append(int(row['N']))
        M.append(int(row['M']))

plt.scatter(N, M)
plt.xlabel('Número de Partículas (N)')
plt.ylabel('M que Minimiza el Tiempo (M)')
plt.title('Relación entre Número de Partículas y M Óptimo')
plt.grid(True)
plt.show()
