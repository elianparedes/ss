import re


def extract_dt(filename, pattern):
    match = re.search(pattern, filename)
    if match:
        return float(match.group(1))
    else:
        raise ValueError(f"No se encontró un valor de dt válido en el nombre del archivo: {filename}")
