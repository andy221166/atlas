#!/bin/bash

# Function to get the current timestamp
timestamp() {
  date +"%Y-%m-%d %H:%M:%S"
}

# Logging functions
log() {
  local message="$1"
  echo -e "$(timestamp) [\033[1;34mINFO\033[0m]: $message" # Blue color for INFO
}

error() {
  local message="$1"
  echo -e "$(timestamp) [\033[1;31mERROR\033[0m]: $message" >&2 # Red color for ERROR
}

debug() {
  local message="$1"
  echo -e "$(timestamp) [\033[1;33mDEBUG\033[0m]: $message" # Yellow color for DEBUG
}
