SET NOCOUNT ON;
SET XACT_ABORT ON;

BEGIN TRY
    BEGIN TRANSACTION;

    /*
      Seed cho database XinNghiPhep.
      Logic khớp với code hiện tại:
      - Users.status = 1 mới đăng nhập được
      - role phải là SUPER_ADMIN / MANAGER / EMPLOYEE
      - LeaveRequests.status phải là PENDING / APPROVED / REJECTED
      - Chỉ request APPROVED/REJECTED mới có LeaveApprovals
      - LeaveBalances.remainingDays = totalDays - usedDays
      - Password đang lưu plain text, không hash

      Nếu DB đã có dữ liệu trùng username/email thì xóa trước hoặc đổi lại giá trị seed.
    */

    IF NOT EXISTS (SELECT 1 FROM Companies WHERE name = N'Công ty ABC')
    BEGIN
        INSERT INTO Companies (name, address, contactInfo)
        VALUES (N'Công ty ABC', N'123 Nguyễn Huệ, Quận 1, TP.HCM', 'contact@abc.local');
    END;

    DECLARE @CompanyId INT =
        (SELECT TOP 1 id FROM Companies WHERE name = N'Công ty ABC' ORDER BY id);

    IF NOT EXISTS (SELECT 1 FROM Users WHERE username = 'superadmin')
    BEGIN
        INSERT INTO Users (username, password, email, fullName, role, status, companyId)
        VALUES ('superadmin', '123456', 'superadmin@example.com', N'Nguyễn Quản Trị', 'SUPER_ADMIN', 1, @CompanyId);
    END;

    IF NOT EXISTS (SELECT 1 FROM Users WHERE username = 'manager01')
    BEGIN
        INSERT INTO Users (username, password, email, fullName, role, status, companyId)
        VALUES ('manager01', '123456', 'manager01@example.com', N'Trần Quản Lý', 'MANAGER', 1, @CompanyId);
    END;

    IF NOT EXISTS (SELECT 1 FROM Users WHERE username = 'employee01')
    BEGIN
        INSERT INTO Users (username, password, email, fullName, role, status, companyId)
        VALUES ('employee01', '123456', 'employee01@example.com', N'Lê Nhân Viên Một', 'EMPLOYEE', 1, @CompanyId);
    END;

    IF NOT EXISTS (SELECT 1 FROM Users WHERE username = 'employee02')
    BEGIN
        INSERT INTO Users (username, password, email, fullName, role, status, companyId)
        VALUES ('employee02', '123456', 'employee02@example.com', N'Phạm Nhân Viên Hai', 'EMPLOYEE', 1, @CompanyId);
    END;

    IF NOT EXISTS (SELECT 1 FROM Users WHERE username = 'employee03')
    BEGIN
        INSERT INTO Users (username, password, email, fullName, role, status, companyId)
        VALUES ('employee03', '123456', 'employee03@example.com', N'Võ Nhân Viên Ba', 'EMPLOYEE', 1, @CompanyId);
    END;

    DECLARE @SuperAdminId INT = (SELECT id FROM Users WHERE username = 'superadmin');
    DECLARE @ManagerId INT = (SELECT id FROM Users WHERE username = 'manager01');
    DECLARE @Employee01Id INT = (SELECT id FROM Users WHERE username = 'employee01');
    DECLARE @Employee02Id INT = (SELECT id FROM Users WHERE username = 'employee02');
    DECLARE @Employee03Id INT = (SELECT id FROM Users WHERE username = 'employee03');
    DECLARE @CurrentYear INT = YEAR(GETDATE());

    IF NOT EXISTS (SELECT 1 FROM LeaveBalances WHERE userId = @SuperAdminId)
    BEGIN
        INSERT INTO LeaveBalances (userId, totalDays, usedDays, remainingDays, lastResetYear)
        VALUES (@SuperAdminId, 12, 0, 12, @CurrentYear);
    END;

    IF NOT EXISTS (SELECT 1 FROM LeaveBalances WHERE userId = @ManagerId)
    BEGIN
        INSERT INTO LeaveBalances (userId, totalDays, usedDays, remainingDays, lastResetYear)
        VALUES (@ManagerId, 12, 2, 10, @CurrentYear);
    END;

    IF NOT EXISTS (SELECT 1 FROM LeaveBalances WHERE userId = @Employee01Id)
    BEGIN
        INSERT INTO LeaveBalances (userId, totalDays, usedDays, remainingDays, lastResetYear)
        VALUES (@Employee01Id, 12, 3, 9, @CurrentYear);
    END;

    IF NOT EXISTS (SELECT 1 FROM LeaveBalances WHERE userId = @Employee02Id)
    BEGIN
        INSERT INTO LeaveBalances (userId, totalDays, usedDays, remainingDays, lastResetYear)
        VALUES (@Employee02Id, 12, 0, 12, @CurrentYear);
    END;

    IF NOT EXISTS (SELECT 1 FROM LeaveBalances WHERE userId = @Employee03Id)
    BEGIN
        INSERT INTO LeaveBalances (userId, totalDays, usedDays, remainingDays, lastResetYear)
        VALUES (@Employee03Id, 12, 1, 11, @CurrentYear);
    END;

    IF NOT EXISTS (
        SELECT 1 FROM LeaveRequests
        WHERE userId = @Employee01Id
          AND startDate = '2026-03-10'
          AND endDate = '2026-03-12'
    )
    BEGIN
        INSERT INTO LeaveRequests (userId, startDate, endDate, reason, status)
        VALUES (@Employee01Id, '2026-03-10', '2026-03-12', N'Nghỉ phép về quê 3 ngày', 'APPROVED');
    END;

    IF NOT EXISTS (
        SELECT 1 FROM LeaveRequests
        WHERE userId = @Employee02Id
          AND startDate = '2026-04-02'
          AND endDate = '2026-04-03'
    )
    BEGIN
        INSERT INTO LeaveRequests (userId, startDate, endDate, reason, status)
        VALUES (@Employee02Id, '2026-04-02', '2026-04-03', N'Nghỉ khám sức khỏe định kỳ', 'PENDING');
    END;

    IF NOT EXISTS (
        SELECT 1 FROM LeaveRequests
        WHERE userId = @Employee03Id
          AND startDate = '2026-03-20'
          AND endDate = '2026-03-20'
    )
    BEGIN
        INSERT INTO LeaveRequests (userId, startDate, endDate, reason, status)
        VALUES (@Employee03Id, '2026-03-20', '2026-03-20', N'Nghỉ việc cá nhân trong ngày', 'REJECTED');
    END;

    IF NOT EXISTS (
        SELECT 1 FROM LeaveRequests
        WHERE userId = @ManagerId
          AND startDate = '2026-03-14'
          AND endDate = '2026-03-15'
    )
    BEGIN
        INSERT INTO LeaveRequests (userId, startDate, endDate, reason, status)
        VALUES (@ManagerId, '2026-03-14', '2026-03-15', N'Nghỉ phép gia đình 2 ngày', 'APPROVED');
    END;

    DECLARE @ApprovedEmpRequestId INT = (
        SELECT TOP 1 id
        FROM LeaveRequests
        WHERE userId = @Employee01Id
          AND startDate = '2026-03-10'
          AND endDate = '2026-03-12'
        ORDER BY id
    );

    DECLARE @RejectedEmpRequestId INT = (
        SELECT TOP 1 id
        FROM LeaveRequests
        WHERE userId = @Employee03Id
          AND startDate = '2026-03-20'
          AND endDate = '2026-03-20'
        ORDER BY id
    );

    DECLARE @ApprovedManagerRequestId INT = (
        SELECT TOP 1 id
        FROM LeaveRequests
        WHERE userId = @ManagerId
          AND startDate = '2026-03-14'
          AND endDate = '2026-03-15'
        ORDER BY id
    );

    IF @ApprovedEmpRequestId IS NOT NULL
       AND NOT EXISTS (SELECT 1 FROM LeaveApprovals WHERE requestId = @ApprovedEmpRequestId)
    BEGIN
        INSERT INTO LeaveApprovals (requestId, managerId, decision, approvalTime, note)
        VALUES (@ApprovedEmpRequestId, @ManagerId, 'APPROVED', '2026-03-08T09:30:00', N'Đã duyệt, bàn giao công việc đầy đủ.');
    END;

    IF @RejectedEmpRequestId IS NOT NULL
       AND NOT EXISTS (SELECT 1 FROM LeaveApprovals WHERE requestId = @RejectedEmpRequestId)
    BEGIN
        INSERT INTO LeaveApprovals (requestId, managerId, decision, approvalTime, note)
        VALUES (@RejectedEmpRequestId, @ManagerId, 'REJECTED', '2026-03-19T16:00:00', N'Thời điểm này phòng đang thiếu người trực.');
    END;

    IF @ApprovedManagerRequestId IS NOT NULL
       AND NOT EXISTS (SELECT 1 FROM LeaveApprovals WHERE requestId = @ApprovedManagerRequestId)
    BEGIN
        INSERT INTO LeaveApprovals (requestId, managerId, decision, approvalTime, note)
        VALUES (@ApprovedManagerRequestId, @SuperAdminId, 'APPROVED', '2026-03-13T10:15:00', N'Super admin đã phê duyệt đơn nghỉ của quản lý.');
    END;

    COMMIT TRANSACTION;
END TRY
BEGIN CATCH
    IF @@TRANCOUNT > 0
        ROLLBACK TRANSACTION;

    THROW;
END CATCH;
