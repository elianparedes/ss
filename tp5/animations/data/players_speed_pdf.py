import os
import numpy as np
import pandas as pd

FILE_NAME = 'futball-vd3.00-tau0.50.csv'
PLAYERS = [('home', '10'), ('home', '5'), ('away', '15')]
DT = 1.0 / 24.0
BIN_WIDTH = 0.4
MAX_SPEED = 8

def calculate_pdf(dataframe):
    speeds = []

    for idx in range(1, len(dataframe)):
        current_row = dataframe.iloc[idx]
        previous_row = dataframe.iloc[idx - 1]

        speed_x = (current_row['x']  - previous_row['x']) / DT
        speed_y = (current_row['y']- previous_row['y']) / DT
        speed = (speed_x ** 2 + speed_y ** 2) ** 0.5

        if speed < MAX_SPEED:
            speeds.append(speed)

    if not speeds:
        return [], []

    bin_edges = np.arange(0, MAX_SPEED, BIN_WIDTH)

    hist, bin_edges = np.histogram(speeds, bins=bin_edges)
    N = len(speeds)
    bin_width = bin_edges[1] - bin_edges[0]

    bin_centers = (bin_edges[:-1] + bin_edges[1:]) / 2

    pdf = hist / (bin_width * N)

    integral = np.trapz(pdf, bin_centers)
    print(integral)
    return bin_centers, pdf

current_dir = os.path.dirname(os.path.abspath(__file__))
input_dir = os.path.join(current_dir, '..', '..', 'output')
file_path = os.path.join(input_dir, FILE_NAME)

data = pd.read_csv(file_path)

all_data = []

for player in PLAYERS:
    player_data = data[(data['particle'] == player[1]) & (data['team'] == player[0])]
    bin_centers, pdf = calculate_pdf(player_data)
    for bin_center, prob in zip(bin_centers, pdf):
        all_data.append({'player': player[1], 'team': player[0], 'bin_center': bin_center, 'probability': prob})

lunatic_data = data[data['particle'] == 'lunatic']
bin_centers, pdf = calculate_pdf(lunatic_data)
for bin_center, prob in zip(bin_centers, pdf):
    all_data.append({'player': 'lunatic', 'team': 'N/A', 'bin_center': bin_center, 'probability': prob})

output_df = pd.DataFrame(all_data)
output_dir = os.path.join(current_dir, 'output')
os.makedirs(output_dir, exist_ok=True)
output_file_path = os.path.join(output_dir, 'player_speeds_pdf.csv')
output_df.to_csv(output_file_path, index=False)
