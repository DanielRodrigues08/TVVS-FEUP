find ./src/test -name "*.java" | while read test_file; do

    class_file=$(echo $test_file | sed 's|/src/test/java/|/src/main/java/|;s|Test.java|.java|')

    echo "SpaceInvaders,$test_file,$class_file" >> tsdetector-input.csv
done