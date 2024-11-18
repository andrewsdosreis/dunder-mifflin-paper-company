#!/bin/bash

# Path to the employees photo folder
PHOTO_DIR="$HOME/dunder-mifflin/photos/employees"

# Endpoint
ENDPOINT="http://localhost:8080/employees"

# Employee data (matching filenames)
employees=(
    "Angela Martin Accounting local-angela.jpg"
    "Creed Bratton Quality_Assurance local-creed.png"
    "Darryl Philbin Warehouse local-darryl.png"
    "David Wallace Management local-david.png"
    "Dwight Schrute Sales local-dwight.jpg"
    "Jan Levinson Management local-jan.png"
    "Jim Halpert Sales local-jim.jpg"
    "Kelly Kapoor Customer_Service local-kelly.jpg"
    "Kevin Malone Accounting local-kevin.png"
    "Meredith Palmer Supplier_Relations local-meredith.png"
    "Michael Scott Manager local-michael.png"
    "Oscar Martinez Accounting local-oscar.png"
    "Pam Beesly Receptionist local-pam.png"
    "Phyllis Vance Sales local-phyllis.png"
    "Roy Anderson Warehouse local-roy.png"
    "Ryan Howard Temp local-ryan.png"
    "Stanley Hudson Sales local-stanley.png"
    "Toby Flenderson HR local-toby.png"
    "Todd Packer Sales local-todd.png"
)

# Iterate over each employee and make a POST request
for employee in "${employees[@]}"; do
    # Split the string into variables
    IFS=' ' read -r firstName lastName department photo <<< "$employee"

    # Construct the full photo path
    photoPath="$PHOTO_DIR/$photo"

    # Check if the photo exists
    if [ -f "$photoPath" ]; then
        # Make the POST request
        curl -X POST "$ENDPOINT" \
            -F "firstName=$firstName" \
            -F "lastName=$lastName" \
            -F "department=$department" \
            -F "photo=@$photoPath"
    else
        echo "Photo not found: $photoPath"
    fi
done
