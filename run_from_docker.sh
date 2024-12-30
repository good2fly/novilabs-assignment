#!/bin/bash

# base name of the input file (assumed to be in the current directory).
input_file="${1:-novi-labs-java-assignment-data.csv}"

input_path="$(pwd)/$input_file"
output_file="${input_file%.csv}-out.csv"
output_dir="out-dir"
output_path="$(pwd)/$output_dir"

docker run -v "$input_path":"/$input_file":ro \
           -v "$output_path":"/$output_dir" \
           --name nvl_homework \
           --rm \
           nv-home-assignment:latest \
           "$input_file" \
           "$output_dir/$output_file"
