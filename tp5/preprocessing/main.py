import math

import pandas as pd
from pandas import DataFrame

def get_positions_by_player(data: DataFrame):
    positions = {}
    positions_columns = data.columns[3:]

    for i in range(0, len(positions_columns), 2):
        player_number = positions_columns[i]

        if player_number not in positions:
            positions[player_number] = []

        x_values = data[positions_columns[i]].values
        y_values = data[positions_columns[i + 1]].values

        positions[player_number].extend(zip(x_values, y_values))

    return positions


home_csv_file = 'input/home.csv'
home_data = pd.read_csv(home_csv_file, skiprows=[0, 1])
home_positions = get_positions_by_player(home_data)

away_csv_file = 'input/away.csv'
away_data = pd.read_csv(away_csv_file, skiprows=[0, 1])
away_positions = get_positions_by_player(away_data)

def get_ranges_with_same_players(data: DataFrame, positions):
    times = data['Time [s]']
    initial_time = times.iloc[0]
    previous_players = set()
    for i, time in enumerate(times):
        frame = i + 1
        current_players = set()

        for player in positions.keys():
            if player == 'Ball':
                continue

            x, y = positions[player][i]
            if not math.isnan(x) and not math.isnan(y):
                current_players.add(player)

        if time != initial_time and current_players != previous_players:
            print('range duration in minutes: ', (time - initial_time) // 60)
            print('frame', frame)
            print('current_count: ', len(current_players))
            print('previous_count: ', len(previous_players))
            print('difference: ', current_players ^ previous_players)
            print()
            initial_time = time

        previous_players = current_players


print('home team ranges')
get_ranges_with_same_players(home_data, home_positions)
print('away team ranges')
get_ranges_with_same_players(away_data, away_positions)