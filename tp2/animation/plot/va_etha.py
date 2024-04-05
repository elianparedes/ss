import matplotlib.pyplot as plt
import pandas as pd

path_to_csv_folder = '../../output/va-etha/'
file_name = 'va-etha_04_04_2024_03_17_34_31.6_4000.csv'
df = pd.read_csv(path_to_csv_folder + file_name, dtype=str)

df['etha'] = pd.to_numeric(df['etha'], errors='coerce')
df['va'] = pd.to_numeric(df['va'], errors='coerce')
df['stdev'] = pd.to_numeric(df['stdev'], errors='coerce')

df.dropna(inplace=True)

n_value = df['n'].unique()[0]
l_value = df['l'].unique()[0]

plt.figure(figsize=(8, 5))
plt.errorbar(df['etha'], df['va'], yerr=df['stdev'], fmt='o', capsize=5)

plt.xlabel('Eta', fontsize=11)
plt.ylabel('Va', fontsize=11)

plt.subplots_adjust(right=0.7)

plt.text(1.05, 0.5, f'N = {n_value}\nL = {l_value}', transform=plt.gca().transAxes, fontsize=11,
         fontname='Times New Roman')

plt.savefig(path_to_csv_folder + 'va-etha' + '-' + str(l_value) + '-' + str(n_value) + '.png')
plt.show()
