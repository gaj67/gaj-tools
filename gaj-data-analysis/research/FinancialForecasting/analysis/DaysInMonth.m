% Number of days in current month.
function ret = DaysInMonth(month, isLeapYear)

persistent DAYS_IN_MONTH
if isempty(DAYS_IN_MONTH)
   DAYS_IN_MONTH = [31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31];
end

ret = DAYS_IN_MONTH(month);
if month == 2, ret = ret + isLeapYear; end

end
