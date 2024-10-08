#!/bin/bash
jar_name="tp2-1.0-SNAPSHOT-jar-with-dependencies.jar"
config_file="config.json"

# Function to check if a command is available
check_command() {
  command -v $1 >/dev/null 2>&1 || {
    echo >&2 "$1 is not installed"
    exit 1
  }
}

check_all_dependencies() {
  check_command java
  check_command mvn
  check_command python3

  echo "All required dependencies are installed."
}

check_if_jar_exists() {
  jar_name="$1"
  if [ -f "target/$jar_name" ]; then
    return 0
  else
    return 1
  fi
}

run_python_script() {
  if [ -f "$1" ]; then
    python3 "$1"
  else
    echo "Error: Script '$1' not found."
  fi
}

# Check if the first argument is provided
if [ -z "$1" ]; then
  echo "Usage: $0 <mode> [options]"
  exit 1
fi

mode="$1"
shift # Move to the next argument

case "$mode" in

config)
  cd animation
  python3 -m venv venv
  source venv/bin/activate
  pip3 install -r requirements.txt
  deactivate
  ;;
simulation)
  mvn -q clean package
  printf "[FILE:config.json] Found config file \n %s" "$(cat $config_file)"
  echo ""
  echo "[INFO] Config can be changed manually via config.json file"

  # General arguments
  variant=""

  # Time arguments
  time_start="500"
  time_end="900"

  # Etha arguments
  etha_start="0"
  etha_max="5"
  etha_step="0.1"

  # Visitors arguments
  conditions="pbc"
  area_radius="1"

  while [[ $# -gt 0 ]]; do
    key="$1"

    case $key in
    # General arguments
    -v | --variant)
      variant="$2"
      shift
      ;;

    # Particles arguments
    --particles-start)
      particles_start="$2"
      shift
      ;;
    --particles-max)
      particles_max="$2"
      shift
      ;;
    --particles-step)
      particles_step="$2"
      shift
      ;;

    # Time arguments
    --time-start)
      time_start="$2"
      shift
      ;;
    --time-end)
      time_end="$2"
      shift
      ;;

    # Etha arguments
    --etha-start)
      etha_start="$2"
      shift
      ;;
    --etha-max)
      etha_max="$2"
      shift
      ;;
    --etha-step)
      etha_step="$2"
      shift
      ;;

    # Visitors arguments
    -c | --conditions)
      conditions="$2"
      shift
      ;;
    -r | --area-radius)
      area_radius="$2"
      shift
      ;;
    *)
      echo "Unknown option: $key"
      exit 1
      ;;
    esac
    shift
  done

  if [ "$variant" = "" ]; then
    echo "[INFO] Running simulation..."
    mvn -q exec:java@default

  elif [ "$variant" = "va-etha" ]; then
    echo "[INFO] Running simulation with va versus etha..."
    mvn -q exec:java@va-etha -Dexec.args="--etha-step=$etha_step --etha-start=$etha_start --etha-max=$etha_max --time-start=$time_start --time-end=$time_end"

  elif [ "$variant" = "va-time" ]; then
    echo "[INFO] Running simulation with va versus time..."
    mvn -q exec:java@va-time -Dexec.args="--etha-step=$etha_step --etha-start=$etha_start --etha-max=$etha_max"

  elif [ "$variant" = "va-rho" ]; then
    echo "[INFO] Running simulation with va versus rho variant..."
    mvn -q exec:java@va-rho -Dexec.args="--particles-start=$particles_start --particles-max=$particles_max --particles-step=$particles_step --time-start=$time_start --time-end=$time_end"

  elif [ "$variant" = "va-time-particles" ]; then
    echo "[INFO] Running simulation with va time variant..."
    mvn -q exec:java@va-rho -Dexec.args="--particles-start=$particles_start --particles-max=$particles_max --particles-step=$particles_step"

  elif [ "$variant" = "visitors" ]; then
    echo "[INFO] Running simulation with visitors variant..."
    mvn -q exec:java@visitors -Dexec.args="--area-radius=$area_radius --conditions=$conditions"

  elif [ "$variant" = "visitors-time-etha" ]; then
    echo "[INFO] Running simulation with visitors time versus etha variant..."
    mvn exec:java@visitors-time-etha -Dexec.args="--area-radius=$area_radius --conditions=$conditions --etha-step=$etha_step --etha-start=$etha_start --etha-max=$etha_max"

  elif [ "$variant" = "visitors-slope-etha" ]; then
    echo "[INFO] Running simulation with visitors slope versus etha variant..."
    mvn exec:java@visitors-slope-etha -Dexec.args="--area-radius=$area_radius --conditions=$conditions --etha-step=$etha_step --etha-start=$etha_start --etha-max=$etha_max"
  fi

  ;;

animation)
  source animation/venv/bin/activate

  # shellcheck disable=SC2164
  cd animation
  while [[ $# -gt 0 ]]; do
    key="$1"

    case $key in
    --va-etha)
      # shellcheck disable=SC2164
      cd plot
      run_python_script "va_etha.py"
      shift
      ;;

    --va-etha-densities)
      # shellcheck disable=SC2164
      cd plot
      run_python_script "va_etha_densities.py"
      shift
      ;;

    --va-rho)
      # shellcheck disable=SC2164
      cd plot
      run_python_script "va_rho.py"
      shift
      ;;

    --va-time)
      # shellcheck disable=SC2164
      cd plot
      run_python_script "va_time.py"
      shift
      ;;

    --va-time-particles)
      # shellcheck disable=SC2164
      cd plot
      run_python_script "va_time_particles.py"
      shift
      ;;

    --visitors-slope-etha)
      # shellcheck disable=SC2164
      cd plot
      run_python_script "visitors_slope_etha.py"
      shift
      ;;

    --visitors-slopes)
      # shellcheck disable=SC2164
      cd plot
      run_python_script "visitors_slopes.py"
      shift
      ;;

    --visitors-time-etha)
      # shellcheck disable=SC2164
      cd plot
      run_python_script "visitors_time_etha.py"
      shift
      ;;

    --visitors-visits-time)
      # shellcheck disable=SC2164
      cd plot
      run_python_script "visitors_visits_time.py"
      shift
      ;;

    --video-default)
      # shellcheck disable=SC2164
      cd videos
      run_python_script "default.py"
      shift
      ;;

    --video-visitors)
      # shellcheck disable=SC2164
      cd videos
      run_python_script "visitors.py"
      shift
      ;;
    *)
      echo "Unknown option: $key"
      exit 1
      ;;
    esac
    shift
  done

  ;;
esac
