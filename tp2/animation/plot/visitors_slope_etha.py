import matplotlib.pyplot as plt
import pandas as pd

from lib.file.csv import get_most_recent_csv

visitors_file = get_most_recent_csv('../../output/visitors-slope-rate/', contains="avg")
df = pd.read_csv(visitors_file)

plt.figure(figsize=(8, 6))
plt.errorbar(df['etha'], df['mean'], yerr=df['stdev'], fmt='o', capsize=5, label='N')
plt.xlabel('Eta')
plt.ylabel('Número de partículas que visitaron / Tiempo (s)')
plt.title(f'N = {df["n"].iloc[0]}, L = {df["l"].iloc[0]}', fontsize=10, loc='right')
plt.grid(True)
plt.tight_layout()
plt.savefig(f"{df['n'].iloc[0]}_{df['l'].iloc[0]}_slope_etha.png")
plt.show()

