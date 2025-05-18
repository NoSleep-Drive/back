#!/bin/bash

VIDEO_DIR="/home/noSleepDrive/videos"

find "$VIDEO_DIR" -maxdepth 1 -type d -regextype posix-extended -regex ".*/[0-9]{4}\.[0-9]{2}\.[0-9]{2}" | while read -r dir
do
    folder_date=$(basename "$dir")

    formatted_date=$(echo "$folder_date" | sed 's/\./-/g')

    if [[ $(date -d "$formatted_date" +%s) -le $(date -d "30 days ago" +%s) ]]; then
        echo "Deleting: $dir"
        rm -rf "$dir"
    fi
done