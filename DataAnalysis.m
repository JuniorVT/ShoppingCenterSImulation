close all;
format long

dataAnalysisRun1 = dataAnalysis('11hours_1_after_fix.csv');
dataAnalysisRun2 = dataAnalysis('11hours_2_after_fix.csv');
dataAnalysisRun3 = dataAnalysis('11hours_3_after_fix.csv');
dataAnalysisRun4 = dataAnalysis('11hours_4_after_fix.csv');
dataAnalysisRun5 = dataAnalysis('11hours_5_after_fix.csv');
dataAnalysisRun6 = dataAnalysis('11hours_6_after_fix.csv');
dataAnalysisRun7 = dataAnalysis('11hours_7_after_fix.csv');
dataAnalysisRun8 = dataAnalysis('11hours_8_after_fix.csv');
dataAnalysisRun9 = dataAnalysis('11hours_9_after_fix.csv');
dataAnalysisRun10 = dataAnalysis('11hours_10_after_fix.csv');

meanDelayRegularCustomersRuns = [dataAnalysisRun1(1), dataAnalysisRun2(1), dataAnalysisRun3(1), dataAnalysisRun4(1), dataAnalysisRun5(1), dataAnalysisRun6(1), dataAnalysisRun7(1), dataAnalysisRun8(1), dataAnalysisRun9(1), dataAnalysisRun10(1)];
meanDelayServiceDeskCustomersRuns = [dataAnalysisRun1(2), dataAnalysisRun2(2), dataAnalysisRun3(2), dataAnalysisRun4(2), dataAnalysisRun5(2), dataAnalysisRun6(2), dataAnalysisRun7(2), dataAnalysisRun8(2), dataAnalysisRun9(2), dataAnalysisRun10(2)];
meanDelayOverallRuns = [dataAnalysisRun1(3), dataAnalysisRun2(3), dataAnalysisRun3(3), dataAnalysisRun4(3), dataAnalysisRun5(3), dataAnalysisRun6(3), dataAnalysisRun7(3), dataAnalysisRun8(3), dataAnalysisRun9(3), dataAnalysisRun10(3)];
averageQueueLengthRuns = [dataAnalysisRun1(4), dataAnalysisRun2(4), dataAnalysisRun3(4), dataAnalysisRun4(4), dataAnalysisRun5(4), dataAnalysisRun6(4), dataAnalysisRun7(4), dataAnalysisRun8(4), dataAnalysisRun9(4), dataAnalysisRun10(4)];

figure;
bar(meanDelayRegularCustomersRuns);
title('Mean delay of regular customers in the 10 runs');

figure;
bar(meanDelayServiceDeskCustomersRuns);
title('Mean delay of service desk customers in the 10 runs');

figure;
bar(meanDelayOverallRuns);
title('Mean delay overall in the 10 runs');

figure;
bar(averageQueueLengthRuns);
title('Average queue length in the 10 runs');


function outputMeasures = dataAnalysis(csvFile)

    collectedData = readmatrix(csvFile);

    currentRow = 1;
    delayRegularCustomers = [];
    delayServiceDeskCustomers = [];
    while currentRow <= size(collectedData, 1)
        if collectedData(currentRow, 4) == 0
            delayRegularCustomers(end+1) = collectedData(currentRow+1, 5) - collectedData(currentRow, 5);
        else
            delayServiceDeskCustomers(end+1) = collectedData(currentRow+1, 5) - collectedData(currentRow, 5);
        end
        currentRow = currentRow + 3;
    end

    delayOverall = cat(2, delayRegularCustomers, delayServiceDeskCustomers);

    currentElement = 1;
    queueLength = 0;
    while currentElement <= numel(delayOverall)
        if delayOverall(currentElement) ~= 0
            queueLength = queueLength + 1;
        end
        currentElement = currentElement + 1;
    end

    meanDelayRegularCustomers = mean(delayRegularCustomers);
    meanDelayServiceDeskCustomers = mean(delayServiceDeskCustomers);
    meanDelayOverall = mean(delayOverall);
    averageQueueLength = queueLength/4;
    
    confidenceIntervalLowerBoundMDRC = meanDelayRegularCustomers - 1.96*sqrt(meanDelayRegularCustomers/length(delayRegularCustomers));
    confidenceIntervalUpperBoundMDRC = meanDelayRegularCustomers + 1.96*sqrt(meanDelayRegularCustomers/length(delayRegularCustomers));
    confidenceIntervalLowerBoundMDSDC = meanDelayServiceDeskCustomers - 1.96*sqrt(meanDelayServiceDeskCustomers/length(delayServiceDeskCustomers));
    confidenceIntervalUpperBoundMDSDC = meanDelayServiceDeskCustomers + 1.96*sqrt(meanDelayServiceDeskCustomers/length(delayServiceDeskCustomers));
    confidenceIntervalLowerBoundMDO = meanDelayOverall - 1.96*sqrt(meanDelayOverall/length(delayOverall));
    confidenceIntervalUpperBoundMDO = meanDelayOverall + 1.96*sqrt(meanDelayOverall/length(delayOverall));
    confidenceIntervalLowerBoundAQL = averageQueueLength - 1.96*sqrt(averageQueueLength/length(queueLength));
    confidenceIntervalUpperBoundAQL = averageQueueLength + 1.96*sqrt(averageQueueLength/length(queueLength));

    fprintf('The mean delay of regular customers is equal to: %4.4f\n', meanDelayRegularCustomers);
    fprintf('The mean delay of service desk customers is equal to: %4.4f\n', meanDelayServiceDeskCustomers);
    fprintf('The mean delay overall is equal to: %4.4f\n', meanDelayOverall);
    fprintf('The average queue length is equal to: %4.4f\n', averageQueueLength);
    fprintf('The 95 percent t-confidence interval for the mean delay of regular customers is: [%4.4f', confidenceIntervalLowerBoundMDRC);
    fprintf(', %4.4f', confidenceIntervalUpperBoundMDRC);
    fprintf(']\nThe 95 percent t-confidence interval for the mean delay of service desk customers is: [%4.4f', confidenceIntervalLowerBoundMDSDC);
    fprintf(', %4.4f', confidenceIntervalUpperBoundMDSDC);
    fprintf(']\nThe 95 percent t-confidence interval for the mean delay overall is: [%4.4f', confidenceIntervalLowerBoundMDO);
    fprintf(', %4.4f', confidenceIntervalUpperBoundMDO);
    fprintf(']\nThe 95 percent t-confidence interval for the average queue length is: [%4.4f', confidenceIntervalLowerBoundAQL);
    fprintf(', %4.4f', confidenceIntervalUpperBoundAQL)
    fprintf(']\n');
    fprintf('\n');
    
    outputMeasures = [meanDelayRegularCustomers, meanDelayServiceDeskCustomers, meanDelayOverall, averageQueueLength, confidenceIntervalLowerBoundMDRC, confidenceIntervalUpperBoundMDRC, confidenceIntervalLowerBoundMDSDC, confidenceIntervalUpperBoundMDSDC, confidenceIntervalLowerBoundMDO, confidenceIntervalUpperBoundMDO, confidenceIntervalLowerBoundAQL, confidenceIntervalUpperBoundAQL];

end