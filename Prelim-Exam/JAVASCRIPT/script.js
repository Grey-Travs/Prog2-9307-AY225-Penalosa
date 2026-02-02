/* * Project: Student Records Manager 
 * Author: Travis Penalosa 
 * Requirement: Prelim Exam 
 */
const CSV_DATA = `StudentID,Name,Grade
2024001,John Doe,85
2024002,Jane Smith,92
2024003,Mike Johnson,78
2024004,Emily Brown,88
2024005,David Lee,95`;

let students = [];
let editIndex = -1; // Tracks if we are editing a record

// --- Initialization ---
function parseCSV(csvString) {
    const lines = csvString.trim().split('\n');
    return lines.slice(1).map(line => {
        const values = line.split(',');
        return {
            studentID: values[0].trim(),
            name: values[1].trim(),
            grade: parseInt(values[2].trim())
        };
    });
}

// --- CRUD Operations ---
function render() {
    const tbody = document.getElementById('table-body');
    tbody.innerHTML = ''; 
    
    students.forEach((student, index) => {
        const row = document.createElement('tr');
        row.className = 'table-row';
        row.innerHTML = `
            <td class="row-number">${index + 1}</td>
            <td class="student-id">${student.studentID}</td>
            <td class="student-name">${student.name}</td>
            <td class="grade ${getGradeClass(student.grade)}">${student.grade}</td>
            <td class="actions">
                <button class="btn-edit" onclick="editRecord(${index})">✏️ Edit</button>
                <button class="btn-delete" onclick="deleteRecord(${index})">🗑️ Delete</button>
            </td>
        `;
        tbody.appendChild(row);
    });
    
    updateStats();
    updateRecordCount();
}

function addRecord() {
    const idInput = document.getElementById('student-id');
    const nameInput = document.getElementById('student-name');
    const gradeInput = document.getElementById('student-grade');
    const addBtn = document.getElementById('add-btn');

    const studentID = idInput.value.trim();
    const name = nameInput.value.trim();
    const grade = parseInt(gradeInput.value);

    // Validation
    if (!studentID || !name || isNaN(grade)) return showNotification('❌ All fields required!', 'error');
    if (grade < 0 || grade > 100) return showNotification('❌ Grade must be 0-100!', 'error');

    if (editIndex === -1) {
        // Create Mode
        if (students.some(s => s.studentID === studentID)) return showNotification('❌ ID already exists!', 'error');
        students.push({ studentID, name, grade });
        showNotification('✅ Record added!', 'success');
    } else {
        // Update Mode
        students[editIndex] = { studentID, name, grade };
        editIndex = -1;
        addBtn.innerHTML = '➕ Add Record';
        addBtn.classList.replace('btn-clear', 'btn-add');
        showNotification('✅ Record updated!', 'success');
    }

    clearFields();
    render();
}

function deleteRecord(index) {
    if (confirm(`Delete ${students[index].name}?`)) {
        students.splice(index, 1);
        render();
        showNotification('🗑️ Record removed', 'info');
    }
}

function editRecord(index) {
    const student = students[index];
    document.getElementById('student-id').value = student.studentID;
    document.getElementById('student-name').value = student.name;
    document.getElementById('student-grade').value = student.grade;
    
    editIndex = index;
    const addBtn = document.getElementById('add-btn');
    addBtn.innerHTML = '💾 Save Changes';
    addBtn.classList.replace('btn-add', 'btn-clear'); 
    window.scrollTo({ top: 0, behavior: 'smooth' });
}

// --- Utilities ---
function getGradeClass(grade) {
    if (grade >= 90) return 'grade-a';
    if (grade >= 80) return 'grade-b';
    if (grade >= 70) return 'grade-c';
    if (grade >= 60) return 'grade-d';
    return 'grade-f';
}

function updateStats() {
    const avgEl = document.getElementById('avg-grade');
    const maxEl = document.getElementById('max-grade');
    const minEl = document.getElementById('min-grade');

    if (students.length === 0) {
        avgEl.textContent = maxEl.textContent = minEl.textContent = '--';
        return;
    }

    const grades = students.map(s => s.grade);
    avgEl.textContent = (grades.reduce((a, b) => a + b, 0) / grades.length).toFixed(1);
    maxEl.textContent = Math.max(...grades);
    minEl.textContent = Math.min(...grades);
}

function updateRecordCount() {
    document.getElementById('record-count').textContent = `Total Records: ${students.length}`;
}

function clearFields() {
    document.getElementById('student-id').value = '';
    document.getElementById('student-name').value = '';
    document.getElementById('student-grade').value = '';
    editIndex = -1;
    document.getElementById('add-btn').innerHTML = '➕ Add Record';
}

function showNotification(msg, type) {
    const notif = document.createElement('div');
    notif.className = `notification ${type}`;
    notif.textContent = msg;
    document.body.appendChild(notif);
    setTimeout(() => notif.remove(), 3000);
}

// --- Listeners ---
document.getElementById('add-btn').addEventListener('click', addRecord);
document.getElementById('clear-btn').addEventListener('click', clearFields);

// Startup
students = parseCSV(CSV_DATA);
render();