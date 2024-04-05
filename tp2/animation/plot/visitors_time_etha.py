import matplotlib.pyplot as plt
import pandas as pd

from lib.file.csv import get_most_recent_csv

visitors_time_etha_file = get_most_recent_csv('../../output/visitors-time-etha/')
data = pd.read_csv(visitors_time_etha_file)

df = pd.DataFrame(data)

plt.figure(figsize=(10, 6))
plt.errorbar(df['etha'], df['mean'], yerr=df['stdev'], fmt='o', capsize=5, label='N=300, L=5')
plt.xlabel('Eta')
plt.ylabel('Tiempo hasta que el 60% de las partículas visiten la zona')
plt.title(f'N = {df["n"].iloc[0]}, L = {df["l"].iloc[0]}, PBC', fontsize=10, loc='right')
plt.grid(True)
plt.tight_layout()
plt.savefig(f"{df['n'].iloc[0]}_{df['l'].iloc[0]}_time_eta.png")
plt.show()
