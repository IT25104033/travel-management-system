package com.dahamdi.travelmanagement.tourpackage.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="tour_package")
public class TourPackage {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name="package_id")
    private Integer packageId;
    @Column(name="package_name",nullable=false,length=150) private String packageName;
    @Column(length=1000) private String description;
    @Column(nullable=false) private Integer duration;
    @Column(nullable=false,precision=12,scale=2) private BigDecimal price;
    @Column(nullable=false,length=30) private String status;

    @OneToMany(mappedBy="tourPackage",cascade=CascadeType.ALL,orphanRemoval=true)
    @OrderBy("dayNumber ASC")
    private List<Itinerary> itineraries=new ArrayList<>();

    public TourPackage(){}
    public Integer getPackageId(){return packageId;}
    public String getPackageName(){return packageName;}
    public void setPackageName(String v){packageName=v;}
    public String getDescription(){return description;}
    public void setDescription(String v){description=v;}
    public Integer getDuration(){return duration;}
    public void setDuration(Integer v){duration=v;}
    public BigDecimal getPrice(){return price;}
    public void setPrice(BigDecimal v){price=v;}
    public String getStatus(){return status;}
    public void setStatus(String v){status=v;}
    public List<Itinerary> getItineraries(){return itineraries;}
}