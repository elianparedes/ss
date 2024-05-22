import pandas as pd
from data.utils import calculate_distance

FIELD_X = 105
FIELD_Y = 68


def create_distance_avg_dataframe(dataframes, variable_name, output_path):
    average_distances = []
    for item in dataframes:
        dataframe = item['dataframe']
        variable_value = item[variable_name]

        avg_distance = dataframe['distance'].mean()
        std_distance = dataframe['distance'].std()

        average_distances.append({
            variable_name: variable_value,
            'avg': avg_distance,
            'std': std_distance
        })

    average_distances_df = pd.DataFrame(average_distances)
    average_distances_df = average_distances_df.sort_values(by=variable_name)
    average_distances_df.to_csv(output_path, index=False)

def create_distance_dataframe(file_path, output_path):
    data = pd.read_csv(file_path)

    lunatic_data = data[data['particle'] == 'lunatic']
    ball_data = data[data['particle'] == 'ball']

    lunatic_data.loc[:, ['x', 'y']] = lunatic_data[['x', 'y']].multiply([FIELD_X, FIELD_Y])
    ball_data.loc[:, ['x', 'y']] = ball_data[['x', 'y']].multiply([FIELD_X, FIELD_Y])

    lunatic_data = lunatic_data.sort_values(by='frame').reset_index(drop=True)
    ball_data = ball_data.sort_values(by='frame').reset_index(drop=True)

    if len(lunatic_data) != len(ball_data):
        raise ValueError("The number of frames does not match between 'lunatic' and 'ball'")

    distances = []
    for i in range(len(lunatic_data)):
        frame = lunatic_data.loc[i, 'frame']
        x1, y1 = lunatic_data.loc[i, ['x', 'y']]
        x2, y2 = ball_data.loc[i, ['x', 'y']]
        distance = calculate_distance(x1, y1, x2, y2)
        distances.append((frame, distance))

    distance_df = pd.DataFrame(distances, columns=['frame', 'distance'])
    distance_df.to_csv(output_path, index=False)

    return distance_df
