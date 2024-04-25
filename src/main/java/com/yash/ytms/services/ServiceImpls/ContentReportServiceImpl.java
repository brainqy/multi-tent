package com.yash.ytms.services.ServiceImpls;

import com.yash.ytms.domain.ContentReport;
import com.yash.ytms.dto.ContentReportDto;
import com.yash.ytms.dto.YtmsUserDto;
import com.yash.ytms.exception.ApplicationException;
import com.yash.ytms.repository.ContentReportRepository;
import com.yash.ytms.services.IServices.ContentReportService;
import com.yash.ytms.services.IServices.IYtmsUserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Description of the class or file.
 *
 * @author Dnyaneshwar Somwanshi
 * @version 1.0
 * @project multi-tent
 * @since 22-03-2024
 */
@Service
public class ContentReportServiceImpl implements ContentReportService {
    @Autowired
    private ContentReportRepository contentReportRepository;
    @Autowired
    private IYtmsUserService userService;
    @Autowired
    ModelMapper modelMapper;
    @Override
    public List<ContentReportDto> getAllReportContents() {
        List<ContentReport> contentReports = contentReportRepository.findAll();
        if (!contentReports.isEmpty()) {
            return contentReports
                    .stream()
                    .map(se -> this
                            .modelMapper
                            .map(se, ContentReportDto.class))
                    .toList();
        } else
            throw new ApplicationException("No Content reports  found !");

    }

    @Override
    public ContentReportDto saveReportContent(ContentReportDto contentReportDto) {
        ContentReport contentRepport = modelMapper.map(contentReportDto, ContentReport.class);
        LocalDateTime timeNOw=LocalDateTime.now();
        contentRepport.setReportedAt(timeNOw);
        contentReportRepository.save(contentRepport);

        ContentReportDto contentFinalDto = modelMapper.map(contentRepport, ContentReportDto.class);
        YtmsUserDto ytmsUserDto = userService.getUserByEmailAdd(contentRepport.getReportedBy());
        contentFinalDto.setReportedBy(ytmsUserDto.getFullName());
        return  contentFinalDto;
    }

    @Override
    public List<HashMap<String, Object>> getContentReportReportedBy() {
        List<HashMap<String, Object>> resultList = new ArrayList<>();

        List<ContentReport> contentReports = contentReportRepository.findAll();
        Map<String, List<ContentReport>> groupedByLink = contentReports.stream()
                .collect(Collectors.groupingBy(ContentReport::getReportedBy));

        groupedByLink.forEach((reportedBy, reports) -> {
            HashMap<String, Object> resultMap = new HashMap<>();
            resultMap.put("reportedBy", reportedBy);
            resultMap.put("reports", reports);
            // Calculate average rating for reports
            double averageRating = calculateAverageRating(reports);
            resultMap.put("averageRating", averageRating);
            resultList.add(resultMap);
        });

        return resultList;
    }

    @Override
    public HashMap getContentReportByLink(String link) {
        HashMap<String, Object> map = new HashMap<>();
        List<ContentReport> contentReports = contentReportRepository.getContentReportyLink(link);
        List<ContentReportDto> contentReportDtos = new ArrayList<>();
         Authentication auth= SecurityContextHolder.getContext().getAuthentication();
         String loggedInUser= auth.getName();
        for (ContentReport contentReport : contentReports) {
            ContentReportDto contentReportDto = modelMapper.map(contentReport, ContentReportDto.class);
            YtmsUserDto ytmsUserDto = userService.getUserByEmailAdd(contentReport.getReportedBy());
            if(contentReport.getReportedBy().equals(loggedInUser)){
                contentReportDto.setOwner(true);
            }else {
                contentReportDto.setOwner(false);
            }
            contentReportDto.setReportedBy(ytmsUserDto.getFullName());
            contentReportDtos.add(contentReportDto);
        }
        map.put("contentDto", contentReportDtos);

        // Calculate average rating (assuming you have a method to do this)
        double avgRating = calculateAverageRating(contentReports);
        map.put("avgRating", avgRating);

        return map;
    }

    @Override
    public List<HashMap<String, Object>> getContentReportByGroupLink() {
        List<HashMap<String, Object>> resultList = new ArrayList<>();

        List<ContentReport> contentReports = contentReportRepository.findAll();
        Map<String, List<ContentReport>> groupedByLink = contentReports.stream()
                .collect(Collectors.groupingBy(ContentReport::getLink));

        groupedByLink.forEach((link, reports) -> {
            HashMap<String, Object> resultMap = new HashMap<>();
            resultMap.put("link", link);
            resultMap.put("reports", reports);
            // Calculate average rating for reports
            double averageRating = calculateAverageRating(reports);
            resultMap.put("averageRating", averageRating);
            resultList.add(resultMap);
        });

        return resultList;
    }

    public double calculateAverageRating(List<ContentReport> contentReports) {
        if (contentReports == null || contentReports.isEmpty()) {
            return 0.0; // Return 0 if there are no reports
        }

        // Calculate the sum of ratings using stream and reduce
        double totalRating = contentReports.stream()
                .mapToDouble(ContentReport::getRating)
                .sum();

        return totalRating / contentReports.size(); // Calculate the average rating
    }
}
