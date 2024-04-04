import matplotlib.pyplot as plt
import pandas as pd
import os

path_to_csv_folder = '../../output/va_time_particles/'
csv_files = [file for file in os.listdir(path_to_csv_folder) if file.endswith('.csv')]

etha_value = None
l_value = None

ps = [1.0,3.0,5.0,7.0,9.0]

plt.figure(figsize=(25, 10))

for csv_file in csv_files:
    file_path = os.path.join(path_to_csv_folder, csv_file)
    data = pd.read_csv(file_path)
    if etha_value is None:
        etha_value = data['etha'].iloc[0]
    if l_value is None:
        l_value = data['l'].iloc[0]
    n_value = data['n'].iloc[0]
    p_value = n_value / (l_value ** 2)

    if p_value in ps:
        plt.plot(data['time'], data['va'], label=f'P = {p_value:.2f}')

plt.xlabel('Tiempo (s)', fontsize=22, fontname='Times New Roman')
plt.ylabel('Va', fontsize=22, fontname='Times New Roman')
plt.title(f'Etha = {etha_value}, L = {l_value}', fontsize=22, fontname='Times New Roman', loc='right')

plt.legend(title='P:', fontsize=22, title_fontsize=22, loc='center left', bbox_to_anchor=(1, 0.5))

plt.xticks(fontsize=22, fontname='Times New Roman')
plt.yticks(fontsize=22, fontname='Times New Roman')

plt.tight_layout()

plt.show()
