import matplotlib.pyplot as plt
import pandas as pd

from lib.file.csv import get_most_recent_csv

visitors_file = get_most_recent_csv('../../output/visitors-slope-rate/', contains="rates")
df = pd.read_csv(visitors_file)

plt.figure(figsize=(12, 6))

plt.plot(df['time'], df['visited_count'], linestyle='-', label='Ya visitaron')
plt.plot(df['time'], df['visiting_count'], linestyle='-', label='Están visitando')

plt.xlabel('Tiempo (s)')
plt.ylabel('Número de partículas')
plt.title(f'N = {df["n"].iloc[0]}, L = {df["l"].iloc[0]}, ETA = 0.2, PBC', fontsize=10, loc='right')
plt.legend(fontsize=8, title_fontsize=8, loc='center left', bbox_to_anchor=(1, 0.5))

plt.grid(True)
plt.tight_layout()
plt.savefig(f"{df['n'].iloc[0]}_{df['l'].iloc[0]}_0.2_PBC_rates.png")
plt.show()
