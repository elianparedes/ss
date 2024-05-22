import os
import glob
from lunatic_distance import create_distance_dataframe
from lunatic_distance import create_distance_avg_dataframe

current_dir = os.path.dirname(os.path.abspath(__file__))
input_dir = os.path.join(current_dir, '..', '..', 'output', 'speed')
output_dir = os.path.join(current_dir, 'output', 'speed')
avg_output_dir = os.path.join(current_dir, 'output', 'avg_distance_speed.csv')

os.makedirs(output_dir, exist_ok=True)
csv_files = glob.glob(os.path.join(input_dir, '*.csv'))

dataframes = []

for file_path in csv_files:
    file_name = os.path.basename(file_path)

    name_parts = file_name.split('-')
    vd_value = float(name_parts[1][2:])
    tau_value = float(name_parts[2][3:-4])

    output_file_name = f'distance-vd{vd_value:.2f}.csv'
    output_path = os.path.join(output_dir, output_file_name)

    distance_df = create_distance_dataframe(file_path, output_path)
    dataframes.append({'vd': vd_value, 'dataframe': distance_df})

create_distance_avg_dataframe(dataframes,'vd',avg_output_dir)
