% Adds extra information to a date structure.
% INPUT:
%   date - The date structure, containing the fields: year; month; and day.
% OUTPUT:
%   date - The date structure containing additional fields such as: isLeapYear; daysFromStart; etc.

function date = DateInfo(date)

date.isLeapYear = IsLeapYear(date.year);
if date.month < 1 || date.month > 12
   error('Invalid date - bad month!');
end
date.daysInMonth = DaysInMonth(date.month, date.isLeapYear);
if date.day < 1 || date.day > date.daysInMonth
   error('Invalid date - bad day!');
end
date.daysSinceYear = DaysSinceYear(date.day, date.month, date.isLeapYear);
date.daysSinceCentury = DaysSinceCentury(date.day, date.month, date.year, date.isLeapYear);

end

%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%5

function ret = IsLeapYear(year)

ret = (mod(year,4) == 0 && (mod(year,100) ~= 0 || mod(year,400) == 0));

end

%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%5

% Number of days in current month.
function ret = DaysInMonth(month, isLeapYear)

persistent DAYS_IN_MONTH
if isempty(DAYS_IN_MONTH)
   DAYS_IN_MONTH = [31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31];
end

ret = DAYS_IN_MONTH(month);
if month == 2, ret = ret + isLeapYear; end

end

%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%5

% Number of days from the start of the year, including current day.
function ret = DaysSinceYear(day, month, isLeapYear)

ret = day;
if month == 1, return; end
for prevMonth = 1:month-1
   ret = ret + DaysInMonth(prevMonth, isLeapYear);
end

end

%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%5

% Number of days from the start of the century, including current day.
% For convenience, the morning of 2000-1-1 is deemed the start of the 21st century.
% This is wrong under the assumption that AD 1 was the 1st year of
% the 1st century.
function ret = DaysSinceCentury(day, month, year, isLeapYear)

ret = DaysSinceYear(day, month, isLeapYear);
baseYear = 100 * floor(year / 100);
ret = ret + (year-baseYear)*365 + sum(IsLeapYear(baseYear:year-1));

end
