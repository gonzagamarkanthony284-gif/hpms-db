# HPMS Quick Launch Guide

## One-Click Launch Options

### Option 1: Full System Check (Recommended)
**File:** `run_hpms.bat`

This script will:
1. ✅ Check Java installation
2. ✅ Verify MySQL is running
3. ✅ Confirm database exists
4. ✅ Stop any existing instances
5. ✅ Launch the application

**How to use:**
- Double-click `run_hpms.bat` in your HPMS folder
- Wait for the application to start
- Login window will appear automatically

### Option 2: Quick Launch (Fast)
**File:** `quick_start.bat`

Direct launch without checks - for when you know everything is ready.

**How to use:**
- Double-click `quick_start.bat`
- Instant application launch

---

## System Requirements

Before launching, ensure:

1. **MySQL Running**
   - Open XAMPP Control Panel
   - Click "Start" next to MySQL
   - Wait for green "Running" status

2. **Database Created**
   - If first time: Run `setup_database.bat`
   - Database `hpms_db` must exist
   - 28 tables will be auto-created

3. **Java Installed**
   - Java 17 or higher required
   - Installed with Eclipse Adoptium (default)

---

## Launch Workflows

### First Time Setup
```
1. Run: setup_database.bat (one-time only)
   └─ Creates database and tables
   
2. Run: run_hpms.bat
   └─ Verifies everything
   └─ Launches application
   
3. Login with:
   Username: admin
   Password: admin123
```

### Daily Usage
```
1. Ensure XAMPP MySQL is started (from Control Panel)

2. Double-click: quick_start.bat

3. Login and use the system

4. Close application when done
```

### Command Line Alternative
```
cd C:\xampp\htdocs\HPMS
java -cp "lib\*;bin" hpms.app.Launcher
```

---

## What Happens When You Launch

1. **Batch Script Runs**
   - Verifies system requirements
   - Checks database connectivity
   - Stops any running instances

2. **Java Application Starts**
   - JDBC connects to `hpms_db`
   - All 28 tables loaded
   - GUI components initialized

3. **Login Window Appears**
   - On your desktop/screen
   - Ready for credentials
   - Database is live and connected

4. **After Login**
   - Main dashboard opens
   - All features available
   - Database operations functional

---

## Creating Windows Shortcuts

### Method 1: Desktop Shortcut (Easy)
1. Navigate to `C:\xampp\htdocs\HPMS`
2. Right-click `run_hpms.bat`
3. Select "Send to" → "Desktop (create shortcut)"
4. Double-click shortcut to launch

### Method 2: Taskbar Pin (Faster)
1. Right-click `run_hpms.bat`
2. Pin to Start or Taskbar
3. Click from Start menu or Taskbar to launch

### Method 3: Custom Shortcut
1. Right-click desktop → "New" → "Shortcut"
2. Target: `C:\xampp\htdocs\HPMS\run_hpms.bat`
3. Name: "HPMS"
4. Finish
5. Double-click to launch

---

## Troubleshooting Quick Launch

### Problem: "Java not found"
**Solution:**
- Install Java 17 from Eclipse Adoptium
- Or modify path in `.bat` file to your Java location

### Problem: "Database not found"
**Solution:**
- Run `setup_database.bat` first
- Ensure MySQL is started in XAMPP

### Problem: Application won't start
**Solution:**
- Run `run_hpms.bat` instead of `quick_start.bat`
- Check error messages in command window
- Verify XAMPP MySQL is running

### Problem: Port already in use
**Solution:**
- The script auto-kills existing Java processes
- If still failing, restart computer

---

## File Locations

```
C:\xampp\htdocs\HPMS\
├── run_hpms.bat          ← Full check + launch
├── quick_start.bat       ← Quick launch
├── setup_database.bat    ← Database setup (one-time)
├── bin/                  ← Compiled Java classes
├── lib/                  ← JDBC driver
└── src/                  ← Source code
```

---

## System Access Points

**After Launching HPMS:**

1. **Application GUI**
   - Login window appears
   - Use admin/admin123
   - Access all features

2. **phpMyAdmin (Web)**
   - URL: http://localhost/phpmyadmin
   - Username: root
   - Password: (empty)
   - View/edit database directly

3. **MySQL Command Line**
   - `mysql -u root hpms_db`
   - Direct SQL commands

---

## Next Steps

1. ✅ Ensure MySQL is running in XAMPP
2. ✅ Double-click `run_hpms.bat`
3. ✅ Wait for login window
4. ✅ Login: admin/admin123
5. ✅ Start using HPMS

**That's it! No more typing commands!**

---

## Batch File Details

### run_hpms.bat
- Full system verification
- Checks Java, MySQL, Database
- Auto-stops previous instances
- Recommended for daily use
- Shows error messages if issues found

### quick_start.bat
- Direct application launch
- No verification
- Fastest startup
- Use when confident everything is ready
- For experienced users

---

**Created:** December 7, 2025  
**HPMS Version:** 1.0  
**Database:** MySQL/MariaDB  
**Status:** Ready to Use
