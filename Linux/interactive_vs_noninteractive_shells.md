# Interactive vs Non-Interactive Shells in Linux

## Table of Contents
1. [What is a Shell?](#what-is-a-shell)
2. [Interactive Shell](#interactive-shell)
3. [Non-Interactive Shell](#non-interactive-shell)
4. [Login vs Non-Login Shells](#login-vs-non-login-shells)
5. [How to Detect Shell Type](#how-to-detect-shell-type)
6. [Configuration Files](#configuration-files)
7. [Why Non-Interactive for User Data?](#why-non-interactive-for-user-data)
8. [Practical Examples](#practical-examples)

---

## What is a Shell?

A shell is a command-line interpreter that:
- Takes commands from you
- Executes them
- Returns results

Common shells: bash, zsh, sh, fish, ksh

---

## Interactive Shell

### Definition
A shell where you (a human) type commands and get immediate responses.

### Characteristics
✅ Reads from user input (keyboard)  
✅ Writes output to terminal  
✅ Waits for your commands  
✅ Shows prompts (like `$` or `#`)  
✅ Has job control (Ctrl+C, Ctrl+Z)  
✅ Command history available  
✅ Tab completion works  
✅ Can use aliases and functions  

### Examples
```bash
# You open a terminal
user@computer:~$ ls
file1.txt  file2.txt

user@computer:~$ echo "Hello"
Hello

user@computer:~$ cd /tmp
user@computer:/tmp$ 
```

### When it happens
- Opening a terminal emulator
- SSH into a server
- Using `su - username`
- Running `bash` without a script

### Interactive Shell Behavior
```bash
# Prompts you for input
$ read -p "Enter name: " name
Enter name: █  # Cursor waits here

# Shows colors and formatting
$ ls --color=auto
file1.txt  directory/  script.sh*

# Keeps history
$ history
1  ls
2  cd /tmp
3  history

# Allows Ctrl+C to cancel
$ sleep 100
^C  # You can interrupt
```

---

## Non-Interactive Shell

### Definition
A shell that runs commands from a script or file without human interaction.

### Characteristics
❌ No user input expected  
❌ No prompts displayed  
❌ Runs start to finish automatically  
❌ No job control (Ctrl+C might not work as expected)  
❌ Limited or no command history  
❌ Typically used for automation  
✅ Faster startup (skips interactive setup)  
✅ Predictable behavior  

### Examples
```bash
# Running a script
$ bash script.sh

# Command via SSH
$ ssh user@server 'ls -la'

# Pipe to bash
$ echo "ls -la" | bash

# Here-document
$ bash << 'EOF'
ls
pwd
EOF

# Cron job
0 2 * * * /usr/local/bin/backup.sh
```

### Non-Interactive Shell Behavior
```bash
# In a script (non-interactive)
#!/bin/bash
# This would FAIL if run non-interactively without -p or input redirection
read name  # No prompt, waits for input from stdin or fails

# This works
name="default_value"  # Hardcoded

# Or check if interactive first
if [ -t 0 ]; then
    read -p "Enter name: " name
else
    name="automated_run"
fi
```

---

## Login vs Non-Login Shells

This is a different dimension from interactive/non-interactive.

### Four Combinations

| Type | Login | Non-Login |
|------|-------|-----------|
| **Interactive** | SSH login, Console login, `su -` | Opening new terminal tab, running `bash` |
| **Non-Interactive** | SSH with command: `ssh user@host cmd` | Running scripts: `bash script.sh` |

### Login Shell
- First shell when you log in
- Reads `/etc/profile`, `~/.bash_profile`, `~/.profile`
- Sets up environment variables (PATH, HOME, etc.)

### Non-Login Shell
- Shells started after login
- Reads `~/.bashrc`
- Inherits environment from parent

### Testing
```bash
# Check if login shell
shopt -q login_shell && echo "Login shell" || echo "Not login shell"

# Or check $0
echo $0
# Output: -bash  (login shell, note the -)
# Output: bash   (non-login shell)
```

---

## How to Detect Shell Type

### Method 1: Check if stdin is a terminal
```bash
#!/bin/bash

if [ -t 0 ]; then
    echo "Interactive: stdin is a terminal"
else
    echo "Non-interactive: stdin is NOT a terminal"
fi
```

### Method 2: Check $- variable
```bash
#!/bin/bash

case $- in
    *i*)
        echo "Interactive shell"
        ;;
    *)
        echo "Non-interactive shell"
        ;;
esac
```

### Method 3: Check PS1 variable
```bash
#!/bin/bash

if [ -z "$PS1" ]; then
    echo "Non-interactive (PS1 not set)"
else
    echo "Interactive (PS1 = $PS1)"
fi
```

### Full Detection Script
```bash
#!/bin/bash

echo "=== Shell Type Detection ==="

# Interactive?
case $- in
    *i*) echo "✓ Interactive shell" ;;
    *)   echo "✗ Non-interactive shell" ;;
esac

# Login?
shopt -q login_shell && echo "✓ Login shell" || echo "✗ Non-login shell"

# Terminal?
[ -t 0 ] && echo "✓ stdin is a terminal" || echo "✗ stdin is NOT a terminal"
[ -t 1 ] && echo "✓ stdout is a terminal" || echo "✗ stdout is NOT a terminal"

# Prompt set?
[ -n "$PS1" ] && echo "✓ PS1 is set: $PS1" || echo "✗ PS1 not set"

# Shell name
echo "Shell: $0"
echo "SHELL variable: $SHELL"
```

---

## Configuration Files

### Bash Configuration File Loading Order

#### Interactive Login Shell
```
1. /etc/profile
2. ~/.bash_profile  (or ~/.bash_login, or ~/.profile - first found)
3. ~/.bashrc (usually sourced from bash_profile)
```

#### Interactive Non-Login Shell
```
1. ~/.bashrc
```

#### Non-Interactive Non-Login Shell
```
1. Nothing automatically
2. Unless BASH_ENV is set
```

#### Non-Interactive Login Shell (rare)
```
1. /etc/profile
2. ~/.bash_profile
```

### Example ~/.bashrc structure
```bash
# ~/.bashrc

# If not running interactively, don't do anything
case $- in
    *i*) ;;
      *) return;;  # Exit early for non-interactive shells
esac

# Interactive-only settings below
HISTSIZE=10000
HISTFILESIZE=20000
shopt -s histappend

# Aliases (only useful in interactive shells)
alias ll='ls -lah'
alias grep='grep --color=auto'

# Prompt
PS1='\u@\h:\w\$ '

# Tab completion
if [ -f /etc/bash_completion ]; then
    . /etc/bash_completion
fi
```

### Example ~/.bash_profile
```bash
# ~/.bash_profile

# Set environment variables (for login shells)
export PATH="$HOME/bin:$PATH"
export EDITOR=vim
export LANG=en_US.UTF-8

# Source .bashrc for interactive sessions
if [ -f ~/.bashrc ]; then
    . ~/.bashrc
fi
```

---

## Why Non-Interactive for User Data?

### The Core Problem: Prompts and User Input

When handling user data programmatically, you want:
1. **Automation** - No human intervention
2. **Predictability** - Same behavior every time
3. **Speed** - No waiting for input
4. **Reliability** - No unexpected pauses

### Real-World Example: User Creation Script

#### ❌ Bad (Interactive Behavior)
```bash
#!/bin/bash

# This would FAIL in automation
read -p "Enter username: " username
read -sp "Enter password: " password

# Waits for human input - BLOCKS automation!
useradd -m "$username"
echo "$username:$password" | chpasswd
```

**Problems:**
- Pauses waiting for input
- Can't run in cron jobs
- Can't process batch operations
- Breaks CI/CD pipelines

#### ✅ Good (Non-Interactive)
```bash
#!/bin/bash

# Accept data non-interactively
USERNAME="$1"
PASSWORD="$2"

# Or from file
# USERNAME=$(cat /tmp/new_user.txt | jq -r '.username')

# Or from environment
# USERNAME="${NEW_USERNAME}"

# No prompts - just execute
useradd -m "$USERNAME"
echo "$USERNAME:$PASSWORD" | chpasswd
```

**Benefits:**
- Runs without human intervention
- Can be automated
- Batch processing possible
- Works in scripts, cron, CI/CD

---

## Common Tools and Non-Interactive Mode

### 1. Package Managers

#### APT (Debian/Ubuntu)
```bash
# Interactive - asks for confirmation
apt install nginx

# Non-interactive - assumes yes
apt install -y nginx
# or
DEBIAN_FRONTEND=noninteractive apt install -y nginx

# Complete non-interactive with no prompts
export DEBIAN_FRONTEND=noninteractive
apt-get update
apt-get install -y -q nginx
apt-get install -y -q --no-install-recommends nginx
```

#### YUM/DNF (RedHat/CentOS)
```bash
# Interactive
yum install nginx

# Non-interactive
yum install -y nginx
```

### 2. MySQL/MariaDB
```bash
# Interactive - prompts for password
mysql -u root -p

# Non-interactive - password in command (insecure but works)
mysql -u root -p"password" -e "CREATE DATABASE mydb;"

# Better - password from file
mysql --defaults-extra-file=/root/.my.cnf -e "CREATE DATABASE mydb;"

# Best - password from environment
export MYSQL_PWD="password"
mysql -u root -e "CREATE DATABASE mydb;"

# Secure setup non-interactively
mysql_secure_installation --use-default
```

### 3. User Management

#### adduser vs useradd
```bash
# adduser - Interactive (asks questions)
adduser john
# Prompts for: password, full name, room number, etc.

# useradd - Non-interactive (command-line options)
useradd -m -s /bin/bash john
echo "john:password123" | chpasswd

# Creating users from file (batch processing)
while IFS=, read -r username password; do
    useradd -m -s /bin/bash "$username"
    echo "$username:$password" | chpasswd
done < users.csv
```

### 4. SSH
```bash
# Interactive - prompts for password
ssh user@server

# Non-interactive - key-based
ssh -i ~/.ssh/id_rsa user@server 'ls -la'

# Non-interactive with password (requires sshpass)
sshpass -p 'password' ssh user@server 'ls -la'

# Non-interactive for host key
ssh -o StrictHostKeyChecking=no user@server
```

### 5. Git
```bash
# Interactive - prompts for credentials
git clone https://github.com/user/repo.git

# Non-interactive - credentials in URL (insecure)
git clone https://username:token@github.com/user/repo.git

# Non-interactive - credential helper
git config --global credential.helper store
```

### 6. Scripts with read commands

#### Making scripts work both ways
```bash
#!/bin/bash

# Detect if interactive
if [ -t 0 ]; then
    # Interactive - ask user
    read -p "Enter database name: " DB_NAME
else
    # Non-interactive - use argument or default
    DB_NAME="${1:-defaultdb}"
fi

echo "Using database: $DB_NAME"
```

---

## Practical Examples

### Example 1: User Data Import Script

```bash
#!/bin/bash
# import_users.sh - Non-interactive user creation

set -e  # Exit on error

USER_FILE="${1:-/var/data/users.json}"

# No prompts - just process
if [ ! -f "$USER_FILE" ]; then
    echo "Error: User file not found: $USER_FILE" >&2
    exit 1
fi

# Process each user non-interactively
jq -c '.users[]' "$USER_FILE" | while read -r user; do
    username=$(echo "$user" | jq -r '.username')
    email=$(echo "$user" | jq -r '.email')
    
    # No confirmation prompts
    useradd -m -s /bin/bash "$username" 2>/dev/null || {
        echo "Warning: User $username already exists" >&2
        continue
    }
    
    # Set initial password (force change on first login)
    echo "$username:changeme123" | chpasswd
    chage -d 0 "$username"
    
    echo "Created user: $username ($email)"
done

echo "User import complete"
```

### Example 2: Database Setup

```bash
#!/bin/bash
# setup_db.sh - Non-interactive database initialization

set -e

DB_NAME="${1:-appdb}"
DB_USER="${2:-appuser}"
DB_PASS="${3:-$(openssl rand -base64 12)}"

# Non-interactive MySQL operations
mysql --defaults-extra-file=/root/.my.cnf << EOF
CREATE DATABASE IF NOT EXISTS ${DB_NAME};
CREATE USER IF NOT EXISTS '${DB_USER}'@'localhost' IDENTIFIED BY '${DB_PASS}';
GRANT ALL PRIVILEGES ON ${DB_NAME}.* TO '${DB_USER}'@'localhost';
FLUSH PRIVILEGES;
EOF

echo "Database setup complete"
echo "DB: $DB_NAME, User: $DB_USER, Pass: $DB_PASS"

# Save credentials non-interactively
cat > /opt/app/db.conf << EOF
DB_HOST=localhost
DB_NAME=${DB_NAME}
DB_USER=${DB_USER}
DB_PASS=${DB_PASS}
EOF

chmod 600 /opt/app/db.conf
```

### Example 3: System Configuration

```bash
#!/bin/bash
# configure_system.sh - Non-interactive system setup

set -e

# Update system non-interactively
export DEBIAN_FRONTEND=noninteractive
apt-get update -qq
apt-get upgrade -y -qq

# Install packages without prompts
apt-get install -y -qq \
    nginx \
    postgresql \
    redis-server \
    supervisor

# Configure services without interaction
systemctl enable nginx
systemctl enable postgresql
systemctl enable redis-server

# Create configuration files programmatically
cat > /etc/nginx/sites-available/app << 'EOF'
server {
    listen 80;
    server_name example.com;
    root /var/www/app;
    index index.html;
}
EOF

ln -sf /etc/nginx/sites-available/app /etc/nginx/sites-enabled/

# Test and reload (non-interactive)
nginx -t && systemctl reload nginx

echo "System configuration complete"
```

### Example 4: Automated Deployment

```bash
#!/bin/bash
# deploy.sh - Non-interactive application deployment

set -e

APP_DIR="/var/www/myapp"
GIT_REPO="https://github.com/user/app.git"
BRANCH="${1:-main}"

# No confirmations - just deploy
cd "$APP_DIR"

# Pull latest code
git fetch origin
git reset --hard "origin/$BRANCH"

# Install dependencies (non-interactive)
export NODE_ENV=production
npm ci --production --quiet

# Build application
npm run build

# Database migrations (non-interactive)
npm run migrate -- --no-interaction

# Restart services
systemctl restart myapp

# Health check
sleep 5
if curl -f http://localhost:3000/health > /dev/null 2>&1; then
    echo "Deployment successful"
else
    echo "Deployment failed - health check failed" >&2
    exit 1
fi
```

---

## Best Practices for Non-Interactive Operations

### 1. Always Provide Defaults
```bash
# Bad
read -p "Enter value: " VALUE

# Good
VALUE="${1:-default_value}"
```

### 2. Use Environment Variables
```bash
# Configuration from environment
DB_HOST="${DB_HOST:-localhost}"
DB_PORT="${DB_PORT:-5432}"
DB_NAME="${DB_NAME:-myapp}"
```

### 3. Accept Input from Files or Streams
```bash
# From file
while IFS= read -r line; do
    process "$line"
done < input.txt

# From pipe
cat data.txt | while IFS= read -r line; do
    process "$line"
done
```

### 4. Use Command-Line Flags
```bash
# Bad
read -p "Verbose? " VERBOSE

# Good
VERBOSE=0
while getopts "v" opt; do
    case $opt in
        v) VERBOSE=1 ;;
    esac
done
```

### 5. Set Error Handling
```bash
#!/bin/bash
set -e          # Exit on error
set -u          # Exit on undefined variable
set -o pipefail # Exit on pipe failure
set -x          # Debug mode (optional)
```

### 6. Use Heredocs for Multi-line Input
```bash
# Non-interactive multi-line input
mysql -u root -p"$PASSWORD" << 'EOF'
CREATE DATABASE app;
USE app;
CREATE TABLE users (id INT, name VARCHAR(100));
EOF
```

### 7. Suppress Interactive Prompts
```bash
# Package manager
apt-get install -y package

# SSH
ssh -o BatchMode=yes user@host

# Git
GIT_TERMINAL_PROMPT=0 git clone repo

# Vim (edit files programmatically)
vim -e -s file.txt << 'EOF'
:%s/old/new/g
:wq
EOF
```

---

## Common Pitfalls

### Pitfall 1: Expecting User Input in Cron
```bash
# BAD - This will hang in cron
#!/bin/bash
read -p "Enter value: " VALUE
process "$VALUE"

# GOOD
#!/bin/bash
VALUE="${1:-default}"
process "$VALUE"
```

### Pitfall 2: Interactive Package Installation
```bash
# BAD - Prompts in automation
apt install mysql-server

# GOOD
DEBIAN_FRONTEND=noninteractive apt install -y mysql-server
```

### Pitfall 3: Assuming Terminal Available
```bash
# BAD - Assumes colors work
echo -e "\e[32mSuccess\e[0m"

# GOOD - Check if terminal
if [ -t 1 ]; then
    echo -e "\e[32mSuccess\e[0m"
else
    echo "Success"
fi
```

### Pitfall 4: Using Interactive Tools in Scripts
```bash
# BAD - Opens interactive editor
vim file.txt

# GOOD - Non-interactive editing
sed -i 's/old/new/g' file.txt
```

---

## Summary

### Interactive Shell
- **Use when:** Human is typing commands
- **Characteristics:** Prompts, colors, history, job control
- **Examples:** Terminal sessions, SSH login

### Non-Interactive Shell  
- **Use when:** Automation, scripts, cron jobs
- **Characteristics:** No prompts, predictable, fast
- **Examples:** Scripts, CI/CD, scheduled tasks

### Why Non-Interactive for User Data?
1. **Automation** - Run without human intervention
2. **Batch Processing** - Handle multiple users/records
3. **Reliability** - No unexpected pauses
4. **CI/CD Integration** - Works in pipelines
5. **Cron Jobs** - Scheduled execution
6. **API Integration** - Programmatic access
7. **Scalability** - Process thousands of records

### Golden Rule
**If it runs in a script, cron job, or automation system, it must be non-interactive.**

Always ask yourself: "What if this runs at 2 AM with no one watching?"
