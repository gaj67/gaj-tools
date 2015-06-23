% Adds extra information to a date structure.
% INPUT:
%   date1 - The 1st date structure.
%   date2 - The 2nd date structure.
% OUTPUT:
%   numDays - The number of days between date1 and date2.

function numDays = DateDiff(date1,date2)

year1 = date1.year; month1 = date1.month; day1 = date1.day;
year2 = date2.year; month2 = date2.month; day2 = date2.day;

sgn = 1;
if year2 < year1
   [year1, year2] = swap(year1, year2);
   [month1, month2] = swap(month1, month2);
   [day1, day2] = swap(day1, day2);
   sgn = -1;
elseif year2 == year1
   if month2 < month1
      [month1, month2] = swap(month1, month2);
      [day1, day2] = swap(day1, day2);
      sgn = -1;
   elseif month2 == month1
      if day2 < day1
         [day1, day2] = swap(day1, day2);
         sgn = -1;
      end
   end
end

days1 = DaysSinceYear(day1, month1, IsLeapYear(year1));
days2 = DaysSinceYear(day2, month2, IsLeapYear(year2));
days = days2 - days1 + (year2-year1)*365 + sum(IsLeapYear(year1:year2-1));
numDays = sgn * days;

end

%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
function [x y] = swap(x,y)
tmp = x;
x = y;
y = tmp;
end
