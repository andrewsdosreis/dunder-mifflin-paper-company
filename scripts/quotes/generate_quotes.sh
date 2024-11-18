#!/bin/bash

# Input and output file names
INPUT_FILE="quotes.txt"
OUTPUT_FILE="quotes.json"

# Initialize variables
echo "[" > $OUTPUT_FILE
current_name=""
first_entry=true

# Read the file line by line
while IFS= read -r line; do
    # Check if the line is a name (assumes no spaces in names)
    if [[ $line =~ ^[A-Za-z]+$ ]]; then
        current_name=$line
    elif [[ -n $line ]]; then
        # If it's not empty, treat it as a quote
        if [ "$first_entry" = false ]; then
            echo "," >> $OUTPUT_FILE
        fi
        first_entry=false
        echo "  {" >> $OUTPUT_FILE
        echo "    \"name\": \"${current_name//_/ }\"," >> $OUTPUT_FILE
        echo "    \"quote\": \"$line\"" >> $OUTPUT_FILE
        echo "  }" >> $OUTPUT_FILE
    fi
done < $INPUT_FILE

# Close the JSON array
echo "]" >> $OUTPUT_FILE

# Notify the user
echo "JSON file has been created: $OUTPUT_FILE"
