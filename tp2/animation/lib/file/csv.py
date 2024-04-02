import glob
import os
from typing import Optional


def get_most_recent_csv(folder_path: str) -> Optional[str]:
    csv_files = glob.glob(os.path.join(folder_path, '*.csv'))

    if not csv_files:
        return None

    csv_files.sort(key=os.path.getmtime, reverse=True)

    most_recent_csv = csv_files[0]
    with open(most_recent_csv, 'r'):
        return most_recent_csv
