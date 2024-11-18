curl -X POST http://localhost:8080/employees \
-F "firstName=Pam" \
-F "lastName=Beesley" \
-F "department=Recepcionist" \
-F "photo=@/Users/andrewsdosreis/dunder-mifflin-employees-photos/pam-beesley.png"

curl -X POST http://localhost:8080/employees \
-F "firstName=Andy" \
-F "lastName=Bernard" \
-F "department=Sales" \
-F "photo=@/Users/andrewsdosreis/dunder-mifflin/photos/new-employees/local-andy.png"

curl -X POST http://localhost:8080/employees \
-F "firstName=Gabe" \
-F "lastName=Lewis" \
-F "department=Corporate" \
-F "photo=@/Users/andrewsdosreis/dunder-mifflin/photos/new-employees/local-gabe.png"

curl -X POST http://localhost:8080/employees \
-F "firstName=Holly" \
-F "lastName=Flax" \
-F "department=HR" \
-F "photo=@/Users/andrewsdosreis/dunder-mifflin/photos/new-employees/local-holly.png"

curl -X POST http://localhost:8080/employees \
-F "firstName=Karen" \
-F "lastName=Filippelli" \
-F "department=Sales" \
-F "photo=@/Users/andrewsdosreis/dunder-mifflin/photos/new-employees/local-karen.png"

curl -X POST http://localhost:8080/employees \
-F "firstName=Robert" \
-F "lastName=California" \
-F "department=CEO" \
-F "photo=@/Users/andrewsdosreis/dunder-mifflin/photos/new-employees/local-robert.png"


curl -X DELETE http://localhost:8080/employees/1 -v
