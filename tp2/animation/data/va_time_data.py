import json
import shutil

import numpy as np
import subprocess
import os

maven_project_dir = '../../'
os.chdir(maven_project_dir)

with open('config.json', 'r') as file:
    original_config = json.load(file)

etha_values = np.arange(0, 5.1, 0.1)

temp_config_dir = './temp_configs'
os.makedirs(temp_config_dir, exist_ok=True)

for etha in etha_values:
    temp_config_path = os.path.join(temp_config_dir, f'config_{etha}.json')
    config = original_config.copy()
    config['etha'] = etha
    with open(temp_config_path, 'w') as temp_file:
        json.dump(config, temp_file)

    command = f'mvn exec:java -Dexec.mainClass="ar.edu.itba.ss.Main" -Dexec.args="-O=output/va_time/{config["cim"]["n"]}_{config["cim"]["l"]}/output_{etha}.csv -C={temp_config_path}"'
    subprocess.run(command, shell=True)

shutil.rmtree(temp_config_dir)
