/* =========================================
   1. DATA & STATE MANAGEMENT
   ========================================= */

// Default Users (If storage is empty)
const DEFAULT_USERS = [
    { username: 'admin', password: '1234567', role: 'admin' },
    { username: 'student', password: 'password123', role: 'student' }
];

// Load Users from LocalStorage or use Default
function getUsers() {
    const stored = localStorage.getItem('app_users');
    return stored ? JSON.parse(stored) : DEFAULT_USERS;
}

// Load Attendance from LocalStorage
function getAttendance() {
    const stored = localStorage.getItem('app_attendance');
    return stored ? JSON.parse(stored) : [];
}

// Save helpers
function saveUser(userObj) {
    const users = getUsers();
    users.push(userObj);
    localStorage.setItem('app_users', JSON.stringify(users));
}

function logAttendance(username) {
    const logs = getAttendance();
    const now = new Date();
    const record = {
        username: username,
        time: now.toLocaleTimeString(),
        date: now.toLocaleDateString(),
        fullDate: now.toLocaleString()
    };
    logs.push(record);
    localStorage.setItem('app_attendance', JSON.stringify(logs));
    return record; // Return for display
}

/* =========================================
   2. AUDIO SYSTEM
   ========================================= */
const audioCtx = new (window.AudioContext || window.webkitAudioContext)();

function playTone(freq, type, startTime, duration) {
    const osc = audioCtx.createOscillator();
    const gain = audioCtx.createGain();
    osc.type = type;
    osc.frequency.value = freq;
    osc.connect(gain);
    gain.connect(audioCtx.destination);
    
    // Smooth envelope
    gain.gain.setValueAtTime(0.1, startTime);
    gain.gain.exponentialRampToValueAtTime(0.001, startTime + duration);
    
    osc.start(startTime);
    osc.stop(startTime + duration);
}

function playWinSound() {
    if(audioCtx.state === 'suspended') audioCtx.resume();
    const now = audioCtx.currentTime;
    // Classic Level Up Arpeggio
    playTone(523.25, 'triangle', now, 0.1);       // C5
    playTone(659.25, 'triangle', now + 0.1, 0.1); // E5
    playTone(783.99, 'triangle', now + 0.2, 0.1); // G5
    playTone(1046.50, 'triangle', now + 0.3, 0.4); // C6
}

function playErrorSound() {
    if(audioCtx.state === 'suspended') audioCtx.resume();
    const now = audioCtx.currentTime;
    const osc = audioCtx.createOscillator();
    const gain = audioCtx.createGain();
    osc.type = 'sawtooth';
    osc.frequency.value = 150;
    osc.connect(gain);
    gain.connect(audioCtx.destination);
    gain.gain.value = 0.1;
    osc.start(now);
    osc.stop(now + 0.2);
}

/* =========================================
   3. CONFETTI ANIMATION (Vanilla JS)
   ========================================= */
function fireConfetti() {
    const canvas = document.getElementById('confetti-canvas');
    const ctx = canvas.getContext('2d');
    canvas.width = window.innerWidth;
    canvas.height = window.innerHeight;

    let particles = [];
    const colors = ['#667eea', '#764ba2', '#e74c3c', '#f1c40f', '#2ecc71'];

    // Create particles
    for (let i = 0; i < 150; i++) {
        particles.push({
            x: window.innerWidth / 2,
            y: window.innerHeight / 2, // Start from center
            w: Math.random() * 10 + 5,
            h: Math.random() * 10 + 5,
            color: colors[Math.floor(Math.random() * colors.length)],
            vx: (Math.random() - 0.5) * 20, // Velocity X
            vy: (Math.random() - 0.5) * 20, // Velocity Y
            gravity: 0.5
        });
    }

    function draw() {
        ctx.clearRect(0, 0, canvas.width, canvas.height);
        let active = false;
        
        particles.forEach(p => {
            p.x += p.vx;
            p.y += p.vy;
            p.vy += p.gravity; // Gravity effect
            
            if (p.y < canvas.height) active = true; // Still visible

            ctx.fillStyle = p.color;
            ctx.fillRect(p.x, p.y, p.w, p.h);
        });

        if (active) requestAnimationFrame(draw);
        else ctx.clearRect(0, 0, canvas.width, canvas.height); // Cleanup
    }
    draw();
}

/* =========================================
   4. DOM & NAVIGATION LOGIC
   ========================================= */

// Views
const views = {
    login: document.getElementById('view-login'),
    signup: document.getElementById('view-signup'),
    student: document.getElementById('view-student'),
    admin: document.getElementById('view-admin')
};

// Inputs
const loginUser = document.getElementById('loginUser');
const loginPass = document.getElementById('loginPass');
const loginError = document.getElementById('loginError');

// Session Data
let currentSessionData = null;

function switchView(viewId) {
    // Hide all
    Object.values(views).forEach(el => {
        el.classList.remove('active');
        el.style.display = 'none';
    });
    // Show Target
    const target = document.getElementById(viewId);
    target.style.display = 'block';
    setTimeout(() => target.classList.add('active'), 10); // Trigger transition
    
    // Clear errors when switching
    loginError.classList.remove('show');
    document.querySelectorAll('.form-group').forEach(g => g.classList.remove('error'));
}

/* =========================================
   5. EVENT HANDLERS
   ========================================= */

// --- LOGIN ---
document.getElementById('loginForm').addEventListener('submit', (e) => {
    e.preventDefault();
    const u = loginUser.value.trim();
    const p = loginPass.value.trim();
    const users = getUsers();

    const foundUser = users.find(user => user.username === u && user.password === p);

    if (foundUser) {
        // SUCCESS
        if (foundUser.role === 'admin') {
            loadAdminDashboard();
        } else {
            loadStudentDashboard(foundUser.username);
        }
    } else {
        // FAIL
        playErrorSound();
        loginError.classList.add('show');
        document.getElementById('loginUserGroup').classList.add('error');
        document.getElementById('loginPassGroup').classList.add('error');
        
        // Shake Effect
        views.login.classList.remove('shake');
        void views.login.offsetWidth;
        views.login.classList.add('shake');
        
        loginPass.value = '';
    }
});

// --- SIGN UP ---
document.getElementById('signupForm').addEventListener('submit', (e) => {
    e.preventDefault();
    const u = document.getElementById('signUser').value.trim();
    const p = document.getElementById('signPass').value.trim();
    
    // Simple validation: Check if user exists
    const users = getUsers();
    if (users.find(user => user.username === u)) {
        alert("Username already taken!");
        return;
    }

    // Save new student
    saveUser({ username: u, password: p, role: 'student' });
    alert("Registration Successful! Please Log In.");
    
    // Reset and switch
    document.getElementById('signUser').value = '';
    document.getElementById('signPass').value = '';
    switchView('view-login');
});

// --- LOAD DASHBOARDS ---

function loadStudentDashboard(username) {
    playWinSound();
    fireConfetti();
    
    const record = logAttendance(username);
    currentSessionData = record;

    document.getElementById('studentNameDisplay').textContent = username;
    document.getElementById('studentTimeDisplay').textContent = record.fullDate;
    
    switchView('view-student');
}

function loadAdminDashboard() {
    const logs = getAttendance();
    const tbody = document.querySelector('#adminTable tbody');
    tbody.innerHTML = ''; // Clear existing

    // Populate Table (Reverse order to show newest first)
    logs.slice().reverse().forEach(log => {
        const row = `<tr>
            <td>${log.username}</td>
            <td>${log.time}</td>
            <td>${log.date}</td>
        </tr>`;
        tbody.innerHTML += row;
    });

    switchView('view-admin');
}

// --- UTILS ---

function handleLogout() {
    loginUser.value = '';
    loginPass.value = '';
    switchView('view-login');
}

function clearAttendanceData() {
    if(confirm("Are you sure you want to delete all attendance records?")) {
        localStorage.removeItem('app_attendance');
        loadAdminDashboard(); // Refresh table
    }
}

function downloadAttendance() {
    if(!currentSessionData) return;
    const content = `ATTENDANCE RECEIPT\n------------------\nName: ${currentSessionData.username}\nTime: ${currentSessionData.time}\nDate: ${currentSessionData.date}\nStatus: Present`;
    const blob = new Blob([content], { type: 'text/plain' });
    const link = document.createElement('a');
    link.href = window.URL.createObjectURL(blob);
    link.download = 'my_attendance.txt';
    link.click();
}