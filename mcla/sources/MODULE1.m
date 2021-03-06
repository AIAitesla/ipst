%
% Copyright (c) 2016, Ioannis Konstantelos <i.konstantelos@imperial.ac.uk>
% and Mingyang Sun <mingyang.sun11@imperial.ac.uk> – Imperial College London,
% Ricerca sul Sistema Energetico – RSE S.p.A. <itesla@rse-web.it>
% This Source Code Form is subject to the terms of the Mozilla Public
% License, v. 2.0. If a copy of the MPL was not distributed with this
% file, You can obtain one at http://mozilla.org/MPL/2.0/.
%

function module1 = MODULE1(Z, K)
%--------------------------------------------------------------
% MODULE1 performs k-means, stores clustered data and computed number of samples to be generated by each cluster-model 
% 
% INPUTS:   -- Z:               [NObs x NVar] matrix of historical data in actual domain
%           -- K:               [scalar] number of clusters
%
% OUTPUTS:  -- module1.Z_c:     [array of cells] Clustered data in actual domain  
%           -- module1.w:       [K x 1] weight of each cluster
%
%--------------------------------------------------------------
%% Size of historical dataset
[NObs, NVar] =  size(Z);  
fprintf('[MODULE1] Original size of historical dataset : %d observations of %d variables \n',NObs,NVar)

%% Check dataset for NaN entries
fprintf('[MODULE1] Checking dataset for NaN entries .. ')
if sum(isnan(Z)) == 0 
    fprintf('0 found\n')
else
    Z(isnan(Z) == 1) = 0;
    fprintf('%d entries found and replaced with zeros\n',sum(isnan(Z)))
end

%% Check dataset for stationary columns and remove them
fprintf('[MODULE1] Checking dataset for stationary variables .. ')

% Find stationary variables
statVars = [];
idx = 0;
for i = 1:NVar
    if all(Z(:,i) == Z(1,i))
        idx = idx + 1;
        % First column contains original column index
        statVars(idx,1) = i;
        % Second column contains the repeated value
        statVars(idx,2) = Z(1,i);
    end
end
% Remove stationary variables from the dataset
if idx == 0 
    fprintf('0 found\n')
else
    % Remove in reverse order to avoid messing up the indexing
    for i = idx:-1:1
        Z(:,statVars(i,1)) = [];
    end  
    fprintf('%d found and removed\n',idx)
    [~, NVar] =  size(Z);  
    fprintf('[MODULE1] %d observations of %d variables will be used to parameterize the TCVine model\n',NObs,NVar)
end

%% K-means clustering according to the seed set in MODULE1_HELPER
fprintf('[MODULE1] Performing k-means clustering (k = %d) ..',K)
tStart = tic;
[idx,~] = kmeans(Z, K);
fprintf(' %.2f seconds\n',toc(tStart));

%% Store clustered data
fprintf('[MODULE1] Storing all clustered historical data ..')
tStart = tic;
% Initialize cell array containing clustered historical data X_c as empty 
Z_c = cell(K,1);
% Save clustered data in each cell
for i = 1:K   
    Z_c{i,1} = Z(idx == i,:);      
end
fprintf(' %.2f seconds\n',toc(tStart));

%% Compute cluster weights w
fprintf('[MODULE1] Computing cluster weights ..')
tStart = tic;
w = zeros(K,1);
for i = 1:K 
    w(i)  = size(Z_c{i,1},1)/NObs;      
end
fprintf(' %.2f seconds\n',toc(tStart));

%% Save output of module 1 to the module1 struct
module1.Z_c      = Z_c;
module1.w        = w;
module1.statVars = statVars;

%% plot clusters
% figure
% hold on
% set1 = 1:round(NVar/2);
% set2 = round(NVar/2) + 1:NVar;
% for i = 1:K
%     scatter(sum(Z(idx==i,set1),2),sum(Z(idx==i,set2),2))
% end

end

