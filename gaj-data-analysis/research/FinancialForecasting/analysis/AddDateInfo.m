% Adds extra information to a data structure.
% INPUT:
%   data - The data structure, containing a Date field.
% OUTPUT:
%   data - The date structure containing additional fields such as: relDate; incPrevDate; etc.

function data = AddDateInfo(data)

global BASE_DATE
if isempty(BASE_DATE)
    BASE_DATE = DateInfo(StrToDate('1900/1/1', 'ymd'));
end

relDate = [];
N = length(data.Date);
for idx=1:N
   date = DateInfo(StrToDate(data.Date{idx},'ymd'));
   relDate = [relDate, DateDiff(BASE_DATE, date)];
end
data.relDate = relDate;

incPrevDate = relDate(1:N-1) - relDate(2:N); % 1st date is most recent.
incPrevDate(N) = NaN;
data.incPrevDate = incPrevDate;

end
