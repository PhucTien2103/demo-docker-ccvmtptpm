BEGIN;

INSERT INTO Companies (name, address, contactInfo)
SELECT 'Cong ty ABC', '123 Nguyen Hue, Quan 1, TP.HCM', 'contact@abc.local'
WHERE NOT EXISTS (
    SELECT 1 FROM Companies WHERE name = 'Cong ty ABC'
);

INSERT INTO Users (username, password, email, fullName, role, status, companyId)
SELECT 'superadmin', '123456', 'superadmin@example.com', 'Nguyen Quan Tri', 'SUPER_ADMIN', 1, c.id
FROM Companies c
WHERE c.name = 'Cong ty ABC'
  AND NOT EXISTS (SELECT 1 FROM Users WHERE username = 'superadmin');

INSERT INTO Users (username, password, email, fullName, role, status, companyId)
SELECT 'manager01', '123456', 'manager01@example.com', 'Tran Quan Ly', 'MANAGER', 1, c.id
FROM Companies c
WHERE c.name = 'Cong ty ABC'
  AND NOT EXISTS (SELECT 1 FROM Users WHERE username = 'manager01');

INSERT INTO Users (username, password, email, fullName, role, status, companyId)
SELECT 'employee01', '123456', 'employee01@example.com', 'Le Nhan Vien Mot', 'EMPLOYEE', 1, c.id
FROM Companies c
WHERE c.name = 'Cong ty ABC'
  AND NOT EXISTS (SELECT 1 FROM Users WHERE username = 'employee01');

INSERT INTO Users (username, password, email, fullName, role, status, companyId)
SELECT 'employee02', '123456', 'employee02@example.com', 'Pham Nhan Vien Hai', 'EMPLOYEE', 1, c.id
FROM Companies c
WHERE c.name = 'Cong ty ABC'
  AND NOT EXISTS (SELECT 1 FROM Users WHERE username = 'employee02');

INSERT INTO Users (username, password, email, fullName, role, status, companyId)
SELECT 'employee03', '123456', 'employee03@example.com', 'Vo Nhan Vien Ba', 'EMPLOYEE', 1, c.id
FROM Companies c
WHERE c.name = 'Cong ty ABC'
  AND NOT EXISTS (SELECT 1 FROM Users WHERE username = 'employee03');

INSERT INTO LeaveBalances (userId, totalDays, usedDays, remainingDays, lastResetYear)
SELECT u.id, 12, 0, 12, EXTRACT(YEAR FROM CURRENT_DATE)::INT
FROM Users u
WHERE u.username = 'superadmin'
  AND NOT EXISTS (SELECT 1 FROM LeaveBalances lb WHERE lb.userId = u.id);

INSERT INTO LeaveBalances (userId, totalDays, usedDays, remainingDays, lastResetYear)
SELECT u.id, 12, 2, 10, EXTRACT(YEAR FROM CURRENT_DATE)::INT
FROM Users u
WHERE u.username = 'manager01'
  AND NOT EXISTS (SELECT 1 FROM LeaveBalances lb WHERE lb.userId = u.id);

INSERT INTO LeaveBalances (userId, totalDays, usedDays, remainingDays, lastResetYear)
SELECT u.id, 12, 3, 9, EXTRACT(YEAR FROM CURRENT_DATE)::INT
FROM Users u
WHERE u.username = 'employee01'
  AND NOT EXISTS (SELECT 1 FROM LeaveBalances lb WHERE lb.userId = u.id);

INSERT INTO LeaveBalances (userId, totalDays, usedDays, remainingDays, lastResetYear)
SELECT u.id, 12, 0, 12, EXTRACT(YEAR FROM CURRENT_DATE)::INT
FROM Users u
WHERE u.username = 'employee02'
  AND NOT EXISTS (SELECT 1 FROM LeaveBalances lb WHERE lb.userId = u.id);

INSERT INTO LeaveBalances (userId, totalDays, usedDays, remainingDays, lastResetYear)
SELECT u.id, 12, 1, 11, EXTRACT(YEAR FROM CURRENT_DATE)::INT
FROM Users u
WHERE u.username = 'employee03'
  AND NOT EXISTS (SELECT 1 FROM LeaveBalances lb WHERE lb.userId = u.id);

INSERT INTO LeaveRequests (userId, startDate, endDate, reason, status)
SELECT u.id, DATE '2026-03-10', DATE '2026-03-12', 'Nghi phep ve que 3 ngay', 'APPROVED'
FROM Users u
WHERE u.username = 'employee01'
  AND NOT EXISTS (
      SELECT 1
      FROM LeaveRequests lr
      WHERE lr.userId = u.id
        AND lr.startDate = DATE '2026-03-10'
        AND lr.endDate = DATE '2026-03-12'
  );

INSERT INTO LeaveRequests (userId, startDate, endDate, reason, status)
SELECT u.id, DATE '2026-04-02', DATE '2026-04-03', 'Nghi kham suc khoe dinh ky', 'PENDING'
FROM Users u
WHERE u.username = 'employee02'
  AND NOT EXISTS (
      SELECT 1
      FROM LeaveRequests lr
      WHERE lr.userId = u.id
        AND lr.startDate = DATE '2026-04-02'
        AND lr.endDate = DATE '2026-04-03'
  );

INSERT INTO LeaveRequests (userId, startDate, endDate, reason, status)
SELECT u.id, DATE '2026-03-20', DATE '2026-03-20', 'Nghi viec ca nhan trong ngay', 'REJECTED'
FROM Users u
WHERE u.username = 'employee03'
  AND NOT EXISTS (
      SELECT 1
      FROM LeaveRequests lr
      WHERE lr.userId = u.id
        AND lr.startDate = DATE '2026-03-20'
        AND lr.endDate = DATE '2026-03-20'
  );

INSERT INTO LeaveRequests (userId, startDate, endDate, reason, status)
SELECT u.id, DATE '2026-03-14', DATE '2026-03-15', 'Nghi phep gia dinh 2 ngay', 'APPROVED'
FROM Users u
WHERE u.username = 'manager01'
  AND NOT EXISTS (
      SELECT 1
      FROM LeaveRequests lr
      WHERE lr.userId = u.id
        AND lr.startDate = DATE '2026-03-14'
        AND lr.endDate = DATE '2026-03-15'
  );

INSERT INTO LeaveApprovals (requestId, managerId, decision, approvalTime, note)
SELECT lr.id, m.id, 'APPROVED', TIMESTAMP '2026-03-08 09:30:00', 'Da duyet, ban giao cong viec day du.'
FROM LeaveRequests lr
JOIN Users e ON e.id = lr.userId AND e.username = 'employee01'
JOIN Users m ON m.username = 'manager01'
WHERE lr.startDate = DATE '2026-03-10'
  AND lr.endDate = DATE '2026-03-12'
  AND NOT EXISTS (SELECT 1 FROM LeaveApprovals la WHERE la.requestId = lr.id);

INSERT INTO LeaveApprovals (requestId, managerId, decision, approvalTime, note)
SELECT lr.id, m.id, 'REJECTED', TIMESTAMP '2026-03-19 16:00:00', 'Thoi diem nay phong dang thieu nguoi truc.'
FROM LeaveRequests lr
JOIN Users e ON e.id = lr.userId AND e.username = 'employee03'
JOIN Users m ON m.username = 'manager01'
WHERE lr.startDate = DATE '2026-03-20'
  AND lr.endDate = DATE '2026-03-20'
  AND NOT EXISTS (SELECT 1 FROM LeaveApprovals la WHERE la.requestId = lr.id);

INSERT INTO LeaveApprovals (requestId, managerId, decision, approvalTime, note)
SELECT lr.id, sa.id, 'APPROVED', TIMESTAMP '2026-03-13 10:15:00', 'Super admin da phe duyet don nghi cua quan ly.'
FROM LeaveRequests lr
JOIN Users m ON m.id = lr.userId AND m.username = 'manager01'
JOIN Users sa ON sa.username = 'superadmin'
WHERE lr.startDate = DATE '2026-03-14'
  AND lr.endDate = DATE '2026-03-15'
  AND NOT EXISTS (SELECT 1 FROM LeaveApprovals la WHERE la.requestId = lr.id);

COMMIT;
