# Complete Guide to Linux Systems and Services

## Table of Contents
1. [Understanding Linux System Architecture](#understanding-linux-system-architecture)
2. [The Init System](#the-init-system)
3. [Systemd - Modern Init System](#systemd---modern-init-system)
4. [Service Management](#service-management)
5. [Creating Custom Services](#creating-custom-services)
6. [System Targets and Runlevels](#system-targets-and-runlevels)
7. [Logging and Monitoring](#logging-and-monitoring)
8. [Common System Services](#common-system-services)
9. [Troubleshooting](#troubleshooting)

---

## Understanding Linux System Architecture

### The Boot Process
1. **BIOS/UEFI** - Hardware initialization
2. **Bootloader (GRUB)** - Loads the kernel
3. **Kernel** - Core of the operating system
4. **Init System** - First process (PID 1), starts all other services
5. **Services/Daemons** - Background processes

### Key Concepts
- **Daemon**: Background process that runs continuously
- **Service**: A daemon managed by the init system
- **Process**: A running instance of a program
- **PID**: Process ID, unique identifier for each process

---

## The Init System

### What is Init?
Init is the first process started by the kernel (always PID 1). It's responsible for:
- Starting system services
- Managing service dependencies
- Handling system shutdown
- Managing system states (runlevels/targets)

### Types of Init Systems
1. **SysVinit** - Traditional, script-based (older systems)
2. **Upstart** - Event-based (Ubuntu older versions)
3. **systemd** - Modern, feature-rich (most current distributions)
4. **OpenRC** - Alternative used in Gentoo, Alpine Linux

---

## Systemd - Modern Init System

### Why Systemd?
- Parallel service startup (faster boot)
- On-demand service activation
- Better dependency management
- Unified logging with journald
- Standard interface across distributions

### Core Components
- **systemd** - Main system and service manager
- **systemctl** - Command to control systemd
- **journalctl** - Query logs from journald
- **systemd-analyze** - Analyze boot performance

### Systemd Architecture
```
systemd (PID 1)
├── Units (services, targets, timers, etc.)
├── journald (logging)
├── logind (login management)
├── networkd (network management)
└── resolved (DNS resolution)
```

---

## Service Management

### Basic systemctl Commands

#### Checking Service Status
```bash
# Check if a service is running
systemctl status servicename

# Check if a service is enabled (starts at boot)
systemctl is-enabled servicename

# Check if a service is active
systemctl is-active servicename

# List all running services
systemctl list-units --type=service --state=running

# List all services (active and inactive)
systemctl list-units --type=service --all

# List failed services
systemctl --failed
```

#### Starting/Stopping Services
```bash
# Start a service
sudo systemctl start servicename

# Stop a service
sudo systemctl stop servicename

# Restart a service
sudo systemctl restart servicename

# Reload service configuration without restart
sudo systemctl reload servicename

# Reload or restart if reload not available
sudo systemctl reload-or-restart servicename
```

#### Enabling/Disabling Services
```bash
# Enable service to start at boot
sudo systemctl enable servicename

# Disable service from starting at boot
sudo systemctl disable servicename

# Enable and start immediately
sudo systemctl enable --now servicename

# Disable and stop immediately
sudo systemctl disable --now servicename
```

#### Masking Services
```bash
# Prevent service from being started (stronger than disable)
sudo systemctl mask servicename

# Unmask a service
sudo systemctl unmask servicename
```

### Unit Files Location
```
/etc/systemd/system/        # Custom/admin created units (highest priority)
/run/systemd/system/        # Runtime units
/lib/systemd/system/        # System-wide units installed by packages
/usr/lib/systemd/system/    # Same as above (different distros)
```

---

## Creating Custom Services

### Basic Service Unit File Structure

Create a file: `/etc/systemd/system/myservice.service`

```ini
[Unit]
Description=My Custom Service
Documentation=https://example.com/docs
After=network.target
Requires=network.target

[Service]
Type=simple
User=username
Group=groupname
WorkingDirectory=/path/to/working/directory
ExecStart=/path/to/executable --options
ExecReload=/bin/kill -HUP $MAINPID
Restart=on-failure
RestartSec=5s

[Install]
WantedBy=multi-user.target
```

### Service Types

1. **simple** (default)
   - Main process is the service
   - systemd considers it started immediately

2. **forking**
   - Service forks a child process
   - Parent exits, child continues
   - Common for traditional daemons

3. **oneshot**
   - Process exits after completion
   - Used for one-time tasks

4. **notify**
   - Service sends notification when ready
   - Requires sd_notify() support

5. **idle**
   - Waits until all jobs are done
   - Used to avoid console output mixing

### Common Service Options

```ini
[Service]
# Restart behavior
Restart=always              # Always restart
Restart=on-failure         # Restart on non-zero exit
Restart=on-abnormal        # Restart on crash/timeout
RestartSec=5s             # Wait before restart

# Environment
Environment="VAR1=value1" "VAR2=value2"
EnvironmentFile=/etc/myservice/config

# Resource limits
LimitNOFILE=65536         # Max open files
MemoryLimit=1G            # Memory limit
CPUQuota=50%             # CPU usage limit

# Security
PrivateTmp=yes           # Private /tmp directory
NoNewPrivileges=yes      # Prevent privilege escalation
ProtectSystem=full       # Read-only /usr and /boot
ProtectHome=yes          # Inaccessible /home
```

### Example: Python Web Service

```ini
[Unit]
Description=Flask Web Application
After=network.target

[Service]
Type=simple
User=webapp
WorkingDirectory=/opt/webapp
Environment="FLASK_APP=app.py"
ExecStart=/usr/bin/python3 -m flask run --host=0.0.0.0
Restart=always
RestartSec=3

[Install]
WantedBy=multi-user.target
```

### Activating Your Service

```bash
# Reload systemd to read new unit file
sudo systemctl daemon-reload

# Start the service
sudo systemctl start myservice

# Enable to start at boot
sudo systemctl enable myservice

# Check status
sudo systemctl status myservice
```

---

## System Targets and Runlevels

### Understanding Targets
Targets are groups of units that represent a system state (similar to runlevels in SysVinit).

### Common Targets

```bash
# Graphical interface (GUI)
multi-user.target + graphical.target

# Multi-user, no GUI (server mode)
multi-user.target

# Rescue mode (single user)
rescue.target

# Emergency mode (minimal)
emergency.target

# Poweroff
poweroff.target

# Reboot
reboot.target
```

### Target Commands

```bash
# Get default target
systemctl get-default

# Set default target
sudo systemctl set-default multi-user.target
sudo systemctl set-default graphical.target

# Change target (temporarily)
sudo systemctl isolate multi-user.target

# List all targets
systemctl list-units --type=target
```

### SysVinit Runlevel Equivalents

| Runlevel | Target           | Description        |
|----------|------------------|--------------------|
| 0        | poweroff.target  | Shutdown           |
| 1        | rescue.target    | Single user mode   |
| 2,3,4    | multi-user.target| Multi-user, no GUI |
| 5        | graphical.target | Multi-user, GUI    |
| 6        | reboot.target    | Reboot             |

---

## Logging and Monitoring

### Journalctl - View Logs

```bash
# View all logs
journalctl

# Follow logs in real-time
journalctl -f

# Logs for specific service
journalctl -u servicename
journalctl -u servicename -f  # Follow

# Logs since boot
journalctl -b
journalctl -b -1  # Previous boot

# Logs for time range
journalctl --since "2024-01-01"
journalctl --since "1 hour ago"
journalctl --since "10:00" --until "11:00"

# Filter by priority
journalctl -p err        # Errors only
journalctl -p warning    # Warnings and above

# Show only kernel messages
journalctl -k

# Show logs in reverse order (newest first)
journalctl -r

# Limit number of lines
journalctl -n 50        # Last 50 lines
journalctl -n 50 -f     # Last 50 + follow

# Disk usage
journalctl --disk-usage

# Clean old logs
sudo journalctl --vacuum-time=7d    # Keep 7 days
sudo journalctl --vacuum-size=1G    # Keep 1GB
```

### Analyzing Boot Performance

```bash
# Show boot time
systemd-analyze

# Show service startup times
systemd-analyze blame

# Show critical chain (slowest path)
systemd-analyze critical-chain

# Create SVG visualization of boot
systemd-analyze plot > boot.svg
```

### Service Dependencies

```bash
# List dependencies of a service
systemctl list-dependencies servicename

# Reverse dependencies (what depends on this service)
systemctl list-dependencies --reverse servicename
```

---

## Common System Services

### Network Services

```bash
# NetworkManager (desktop systems)
systemctl status NetworkManager

# systemd-networkd (minimal systems)
systemctl status systemd-networkd

# DNS resolver
systemctl status systemd-resolved

# SSH server
systemctl status sshd  # or ssh
```

### Web Servers

```bash
# Apache
systemctl status apache2    # Debian/Ubuntu
systemctl status httpd      # RHEL/CentOS

# Nginx
systemctl status nginx
```

### Database Services

```bash
# MySQL/MariaDB
systemctl status mysql      # Debian/Ubuntu
systemctl status mariadb    # RHEL/CentOS

# PostgreSQL
systemctl status postgresql

# MongoDB
systemctl status mongod
```

### System Services

```bash
# Cron (scheduled tasks)
systemctl status cron       # Debian/Ubuntu
systemctl status crond      # RHEL/CentOS

# System logging
systemctl status rsyslog

# Time synchronization
systemctl status systemd-timesyncd
systemctl status chronyd    # Alternative NTP

# D-Bus (inter-process communication)
systemctl status dbus
```

---

## Troubleshooting

### Debugging Service Failures

1. **Check service status**
```bash
systemctl status servicename
```

2. **View recent logs**
```bash
journalctl -u servicename -n 100
journalctl -u servicename -f
```

3. **Check if service is enabled**
```bash
systemctl is-enabled servicename
```

4. **Verify unit file syntax**
```bash
systemd-analyze verify /etc/systemd/system/myservice.service
```

5. **Check dependencies**
```bash
systemctl list-dependencies servicename
```

### Common Issues

#### Service fails to start
```bash
# Check logs for error messages
journalctl -u servicename -n 50

# Verify file permissions
ls -l /path/to/service/files

# Check if port is already in use
sudo netstat -tulpn | grep :80
sudo ss -tulpn | grep :80

# Test the executable manually
/path/to/executable --options
```

#### Service starts but stops immediately
```bash
# Often an issue with Type= setting
# Try changing Type=forking to Type=simple

# Check if process is actually forking
# Add RemainAfterExit=yes for oneshot services
```

#### Service doesn't start at boot
```bash
# Verify it's enabled
systemctl is-enabled servicename

# Enable it
sudo systemctl enable servicename

# Check target dependencies
systemctl list-dependencies graphical.target
```

### System Won't Boot

1. **Boot into rescue mode** - Select from GRUB menu
2. **Check system logs**
```bash
journalctl -b -p err
```
3. **Disable problematic service**
```bash
systemctl disable problematic-service
```

### Performance Issues

```bash
# Check which services are consuming resources
systemctl status

# Show service CPU/memory usage
systemd-cgtop

# Check failed services
systemctl --failed

# Analyze boot time
systemd-analyze blame
```

---

## Best Practices

1. **Always reload systemd after editing unit files**
   ```bash
   sudo systemctl daemon-reload
   ```

2. **Test services before enabling at boot**
   ```bash
   sudo systemctl start myservice
   # Test it works
   sudo systemctl enable myservice
   ```

3. **Use descriptive names and documentation**
   - Name services clearly
   - Add Description= and Documentation= in unit files

4. **Set appropriate restart policies**
   - Use Restart=on-failure for production services
   - Set reasonable RestartSec values

5. **Implement proper logging**
   - Ensure services log to journald
   - Set appropriate log levels

6. **Use systemd security features**
   - PrivateTmp=yes
   - NoNewPrivileges=yes
   - ProtectSystem and ProtectHome

7. **Monitor service health**
   - Regularly check failed services
   - Review logs periodically
   - Use systemd-cgtop for resource monitoring

---

## Quick Reference

### Essential Commands Cheat Sheet

```bash
# Status and Information
systemctl status SERVICE
systemctl list-units --type=service
systemctl --failed

# Control Services
sudo systemctl start SERVICE
sudo systemctl stop SERVICE
sudo systemctl restart SERVICE
sudo systemctl reload SERVICE

# Boot Configuration
sudo systemctl enable SERVICE
sudo systemctl disable SERVICE
sudo systemctl enable --now SERVICE

# Logs
journalctl -u SERVICE
journalctl -f
journalctl -b

# System Analysis
systemd-analyze
systemd-analyze blame
systemd-analyze critical-chain

# Reload Configuration
sudo systemctl daemon-reload
```

---

## Additional Resources

- Official systemd documentation: https://www.freedesktop.org/wiki/Software/systemd/
- Systemd man pages: `man systemd`, `man systemctl`, `man journalctl`
- Red Hat systemd guide: https://access.redhat.com/documentation/
- Arch Linux systemd guide: https://wiki.archlinux.org/title/Systemd

---

## Practice Exercises

1. Create a simple service that runs a bash script
2. Configure a web server to start automatically at boot
3. Investigate why a failed service isn't starting
4. Analyze your system's boot time and identify slow services
5. Create a timer unit to run a task periodically
6. Implement resource limits on a service

This guide covers the fundamentals of Linux systems and services. Practice with these commands and concepts to build your expertise!
