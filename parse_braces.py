with open('app/src/main/java/com/plancraft/android/ui/projects/ProjectsScreen.kt', 'r') as f:
    lines = f.readlines()

count = 0
for line_num, line in enumerate(lines, 1):
    for char in line:
        if char == '{':
            count += 1
        elif char == '}':
            count -= 1
        
        if count < 0:
            print(f"Extra closing brace at line {line_num}")
            count = 0  # reset to find more

if count > 0:
    print(f"Unclosed opening brace! count: {count}")
elif count == 0:
    print("Braces are balanced globally")
