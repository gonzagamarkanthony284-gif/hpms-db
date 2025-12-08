// Toggle Password Visibility
function togglePassword() {
    const passwordInput = document.getElementById('password');
    const toggleIcon = document.querySelector('.toggle-password');
    
    if (passwordInput.type === 'password') {
        passwordInput.type = 'text';
        toggleIcon.textContent = '🙈';
    } else {
        passwordInput.type = 'password';
        toggleIcon.textContent = '👁️';
    }
}

// Handle Forgot Password
function handleForgotPassword(event) {
    event.preventDefault();
    
    // Show a custom modal or alert
    const email = document.getElementById('email').value;
    
    if (email) {
        showNotification(
            'Password Reset Request',
            `A password reset link will be sent to ${email} if an account exists with this email address. Please check your email inbox and spam folder.`,
            'info'
        );
    } else {
        showNotification(
            'Email Required',
            'Please enter your email address first to reset your password.',
            'warning'
        );
        document.getElementById('email').focus();
    }
}

// Form Validation
function validateEmail(email) {
    const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    return emailRegex.test(email);
}

function validatePassword(password) {
    return password.length >= 6;
}

function showError(inputId, message) {
    const input = document.getElementById(inputId);
    const errorElement = document.getElementById(`${inputId}Error`);
    
    input.classList.add('error');
    errorElement.textContent = message;
    errorElement.classList.add('show');
}

function clearError(inputId) {
    const input = document.getElementById(inputId);
    const errorElement = document.getElementById(`${inputId}Error`);
    
    input.classList.remove('error');
    errorElement.textContent = '';
    errorElement.classList.remove('show');
}

// Real-time validation
document.getElementById('email')?.addEventListener('input', function(e) {
    clearError('email');
    
    if (e.target.value && !validateEmail(e.target.value)) {
        showError('email', 'Please enter a valid email address');
    }
});

document.getElementById('password')?.addEventListener('input', function(e) {
    clearError('password');
    
    if (e.target.value && !validatePassword(e.target.value)) {
        showError('password', 'Password must be at least 6 characters long');
    }
});

// Form Submission
document.getElementById('loginForm')?.addEventListener('submit', async function(e) {
    e.preventDefault();
    
    // Clear previous errors
    clearError('email');
    clearError('password');
    
    // Get form values
    const email = document.getElementById('email').value.trim();
    const password = document.getElementById('password').value;
    const rememberMe = document.getElementById('rememberMe').checked;
    
    // Validate inputs
    let hasError = false;
    
    if (!email) {
        showError('email', 'Email address is required');
        hasError = true;
    } else if (!validateEmail(email)) {
        showError('email', 'Please enter a valid email address');
        hasError = true;
    }
    
    if (!password) {
        showError('password', 'Password is required');
        hasError = true;
    } else if (!validatePassword(password)) {
        showError('password', 'Password must be at least 6 characters long');
        hasError = true;
    }
    
    if (hasError) {
        return;
    }
    
    // Show loading state
    const loginBtn = document.querySelector('.login-btn');
    const btnText = document.getElementById('loginBtnText');
    const btnLoader = document.getElementById('loginBtnLoader');
    
    loginBtn.disabled = true;
    btnText.style.display = 'none';
    btnLoader.style.display = 'block';
    
    try {
        // Simulate API call (replace with actual authentication)
        await authenticateUser(email, password, rememberMe);
        
    } catch (error) {
        // Reset button state
        loginBtn.disabled = false;
        btnText.style.display = 'block';
        btnLoader.style.display = 'none';
        
        showNotification(
            'Login Failed',
            error.message || 'Invalid email or password. Please try again.',
            'error'
        );
    }
});

// Authentication Function (Replace with actual API call)
async function authenticateUser(email, password, rememberMe) {
    return new Promise((resolve, reject) => {
        setTimeout(() => {
            // Mock authentication logic
            // Replace this with actual backend API call
            
            // Example: Check against hardcoded credentials (for demonstration only)
            const mockUsers = [
                { email: 'admin@medicarehospital.com', password: 'admin123', role: 'admin' },
                { email: 'doctor@medicarehospital.com', password: 'doctor123', role: 'doctor' },
                { email: 'nurse@medicarehospital.com', password: 'nurse123', role: 'nurse' }
            ];
            
            const user = mockUsers.find(u => u.email === email && u.password === password);
            
            if (user) {
                // Store session (in real app, use secure tokens)
                if (rememberMe) {
                    localStorage.setItem('userEmail', email);
                    localStorage.setItem('rememberMe', 'true');
                } else {
                    sessionStorage.setItem('userEmail', email);
                }
                
                localStorage.setItem('userRole', user.role);
                
                showNotification(
                    'Login Successful',
                    'Welcome back! Redirecting to your dashboard...',
                    'success'
                );
                
                // Redirect to dashboard after 1.5 seconds
                setTimeout(() => {
                    // Replace with actual dashboard URL
                    window.location.href = 'dashboard.html';
                }, 1500);
                
                resolve(user);
            } else {
                reject(new Error('Invalid email or password. Please check your credentials and try again.'));
            }
        }, 1500); // Simulate network delay
    });
}

// Notification System
function showNotification(title, message, type = 'info') {
    // Remove existing notifications
    const existingNotification = document.querySelector('.notification');
    if (existingNotification) {
        existingNotification.remove();
    }
    
    // Create notification element
    const notification = document.createElement('div');
    notification.className = `notification notification-${type}`;
    notification.innerHTML = `
        <div class="notification-content">
            <div class="notification-icon">${getNotificationIcon(type)}</div>
            <div class="notification-text">
                <strong>${title}</strong>
                <p>${message}</p>
            </div>
            <button class="notification-close" onclick="this.parentElement.parentElement.remove()">×</button>
        </div>
    `;
    
    // Add styles
    const style = document.createElement('style');
    style.textContent = `
        .notification {
            position: fixed;
            top: 20px;
            right: 20px;
            max-width: 400px;
            background: white;
            border-radius: 10px;
            box-shadow: 0 10px 30px rgba(0, 0, 0, 0.2);
            z-index: 10000;
            animation: slideInRight 0.4s ease-out;
        }
        
        @keyframes slideInRight {
            from {
                transform: translateX(100%);
                opacity: 0;
            }
            to {
                transform: translateX(0);
                opacity: 1;
            }
        }
        
        .notification-content {
            display: flex;
            align-items: flex-start;
            gap: 15px;
            padding: 20px;
        }
        
        .notification-icon {
            font-size: 2rem;
            flex-shrink: 0;
        }
        
        .notification-text {
            flex: 1;
        }
        
        .notification-text strong {
            display: block;
            font-size: 1rem;
            margin-bottom: 5px;
            color: #1e293b;
        }
        
        .notification-text p {
            font-size: 0.9rem;
            color: #64748b;
            margin: 0;
            line-height: 1.5;
        }
        
        .notification-close {
            background: none;
            border: none;
            font-size: 1.5rem;
            color: #94a3b8;
            cursor: pointer;
            padding: 0;
            width: 30px;
            height: 30px;
            display: flex;
            align-items: center;
            justify-content: center;
            border-radius: 5px;
            transition: all 0.3s ease;
        }
        
        .notification-close:hover {
            background: #f1f5f9;
            color: #475569;
        }
        
        .notification-success {
            border-left: 4px solid #10b981;
        }
        
        .notification-error {
            border-left: 4px solid #ef4444;
        }
        
        .notification-warning {
            border-left: 4px solid #f59e0b;
        }
        
        .notification-info {
            border-left: 4px solid #3b82f6;
        }
        
        @media (max-width: 480px) {
            .notification {
                top: 10px;
                right: 10px;
                left: 10px;
                max-width: none;
            }
        }
    `;
    
    if (!document.querySelector('style[data-notification-styles]')) {
        style.setAttribute('data-notification-styles', 'true');
        document.head.appendChild(style);
    }
    
    // Add to page
    document.body.appendChild(notification);
    
    // Auto remove after 5 seconds
    setTimeout(() => {
        notification.remove();
    }, 5000);
}

function getNotificationIcon(type) {
    const icons = {
        success: '✅',
        error: '❌',
        warning: '⚠️',
        info: 'ℹ️'
    };
    return icons[type] || icons.info;
}

// Load remembered email on page load
document.addEventListener('DOMContentLoaded', function() {
    const rememberMe = localStorage.getItem('rememberMe');
    const savedEmail = localStorage.getItem('userEmail');
    
    if (rememberMe === 'true' && savedEmail) {
        document.getElementById('email').value = savedEmail;
        document.getElementById('rememberMe').checked = true;
    }
    
    // Add smooth scrolling
    document.querySelectorAll('a[href^="#"]').forEach(anchor => {
        anchor.addEventListener('click', function(e) {
            e.preventDefault();
            const target = document.querySelector(this.getAttribute('href'));
            if (target) {
                target.scrollIntoView({
                    behavior: 'smooth',
                    block: 'start'
                });
            }
        });
    });
});

// Prevent form resubmission on page refresh
if (window.history.replaceState) {
    window.history.replaceState(null, null, window.location.href);
}

// Keyboard shortcuts
document.addEventListener('keydown', function(e) {
    // Clear form with Ctrl+K
    if (e.ctrlKey && e.key === 'k') {
        e.preventDefault();
        document.getElementById('loginForm').reset();
        clearError('email');
        clearError('password');
    }
});

console.log('Hospital Management System - Login Page Loaded');
console.log('Version: 1.0.0');
console.log('Note: Doctor accounts are created by administrators only.');
