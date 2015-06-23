% Converts a date string into a date structure.
% INPUTS:
%   str - The string containing the date in the simple format: 
%         <part1> <sep> <part2> <sep> <part3>, where the separator
%         <sep> is inferred from the input string.
%   fmt - The format of the date, specified as a string:
%         * ymd => European-style dates, e.g. 2012-09-23.
%         * dmy => Australian-style dates, e.g. 23/9/2012.
%         * mdy => American-style dates, e.g. 9.23.2012.
% OUTPUT:
%   date - The date structure containing fields: year; month; and day.

function date = StrToDate(str,fmt)

if ~isempty(strfind(str,'-'))
   sep = '-';
elseif ~isempty(strfind(str,'/'))
   sep = '/';
elseif ~isempty(strfind(str,'.'))
   sep = '.';
elseif ~isempty(strfind(str,' '))
   sep = ' ';
else
   error('Unable to guess date separator for: ' + str);
end

parts = SplitStr(str,sep);
if length(parts) ~= 3
   error('Invalid date format: ' + str);
end
switch (fmt)
   case 'ymd'
      yearIndex = 1; monthIndex = 2; dayIndex = 3;
   case 'dmy'
      yearIndex = 3; monthIndex = 2; dayIndex = 1;
   case 'mdy'
      yearIndex = 3; monthIndex = 1; dayIndex = 2;
   otherwise
      error('Invalid date format: ' + fmt);
end
date.year = eval(parts{yearIndex});
date.month = eval(parts{monthIndex});
date.day = eval(parts{dayIndex});

end
