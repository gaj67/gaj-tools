% Test whether or not the gaps between trading days 
% (e.g. due to weekends, holidays or other closures)
% are significant.
function Test1(data)

% Null hypothesis is that the mean log rate-of-return is zero.
N = length(data.Open);
rate = log(data.Open(1:N-1) ./ data.Close(2:N));
mu = mean(rate);
v = var(rate);
printf('Overall: N=%d, mean=%e, var=%e, min=%e, max=%e\n', N, mu, v, min(rate), max(rate));
t = mu / sqrt(v/N);
printf('t=%e, df=%d\n', t, N-1);

% Null hypothesis is that the means of consecutive and non-cosecutive
% log rate-of-returns are equal.
con = find(data.incPrevDate==1); % consecutive.
r_con = rate(con);
N_con = length(r_con);
mu_con = mean(r_con);
v_con = var(r_con);
printf('Consecutive: N=%d, mean=%e, var=%e, min=%e, max=%e\n', N_con, mu_con, v_con, min(r_con), max(r_con));
t = mu_con / sqrt(v_con/N_con);
printf('t=%e, df=%d\n', t, N_con-1);

non = find(~isnan(data.incPrevDate) && data.incPrevDate~=1); % non-consecutive.
r_non = rate(non);
N_non = length(r_non);
mu_non = mean(r_non);
v_non = var(r_non);
printf('Non-consecutive: N=%d, mean=%e, var=%e, min=%e, max=%e\n', N_non, mu_non, v_non, min(r_non), max(r_non));
t = mu_non / sqrt(v_non/N_non);
printf('t=%e, df=%d\n', t, N_non-1);

v_pooled = ((N_con-1)*v_con + (N_non-1)*v_non) / (N_con + N_non - 2);
mu_diff = mu_con-mu_non;
v_diff = v_pooled*(1/N_con+1/N_non);
printf('mean_diff=%e, var_pooled=%e, var_diff=%e\n', mu_diff, v_pooled, v_diff);
t = mu_diff / sqrt(v_diff);
printf('t=%e, df=%d\n', t, N_con+N_non-2);

figure;
plot(data.relDate(con),r_con,'g',data.relDate(non),r_non,'r');
