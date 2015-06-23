% Number of days from the start of the year, including current day.
function ret = DaysSinceYear(day, month, isLeapYear)

ret = day;
if month == 1, return; end
for prevMonth = 1:month-1
   ret = ret + DaysInMonth(prevMonth, isLeapYear);
end

end
