import csv
import matplotlib.pyplot as plt

# Leer datos del CSV
N = []
M = []
def plot_best_m_analytic(file_str:str):
    with open(file_str, 'r') as file:
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

cim = '../../output/analytics_cim.csv'
plot_best_m_analytic(cim)
bf = '../../output/analytics_bf.csv'
plot_best_m_analytic(bf)
