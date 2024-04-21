import os

import pandas as pd
import numpy as np

DT = 0.0002


def calculate_dt(csv_name: str):
    data = pd.read_csv(csv_name)
    ball_data = data[data['id'] == 1]

    if ball_data.empty:
        print("No data for ball with id=1.")
        return

    ball_collisions = []
    previous_row = ball_data.iloc[0]  # Asume que al menos hay una fila con id=1

    for index, row in ball_data.iloc[1:].iterrows():
        vx = row['vx']
        vy = row['vy']
        vx_prev = previous_row['vx']
        vy_prev = previous_row['vy']

        if vx != vx_prev or vy != vy_prev:
            ball_collisions.append(row['time'])

        previous_row = row

    if len(ball_collisions) > 1:
        print(len(ball_collisions))
        diff = np.diff(ball_collisions)
        print("Average time between collisions:", np.mean(diff))
    else:
        print("Not enough collision data to calculate average.")


def compute(csv_name: str, output_csv_name):
    data = pd.read_csv(csv_name)
    ball_data = data[data['id'] == 1]
    initial = ball_data.iloc[0]
    real_times = []
    interval_times = []
    msds = []

    for index, row in ball_data.iloc[1:].iterrows():
        x = row['x']
        y = row['y']
        current_time = row['time']
        if current_time >= len(real_times) * DT:
            msds.append((x - initial['x']) ** 2 + (y - initial['y']) ** 2)
            interval_times.append(len(real_times) * DT)
            real_times.append(current_time)

    results_df = pd.DataFrame({
        'Time': real_times,
        'Real Time': interval_times,
        'MSD': msds
    })

    results_df.to_csv(output_csv_name, index=False)

    return interval_times, msds


input_directory = '../../output/msd/v10/'
output_directory = './msd/v10/'

#calculate_dt(input_directory+'msd_1.csv')

for filename in os.listdir(input_directory):
    if filename.endswith('.csv'):
        input_path = os.path.join(input_directory, filename)
        output_path = os.path.join(output_directory, 'processed_' + filename)
        compute(input_path, output_path)
