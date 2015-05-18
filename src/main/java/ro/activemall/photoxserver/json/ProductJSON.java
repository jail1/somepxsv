package ro.activemall.photoxserver.json;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import ro.activemall.photoxserver.entities.Product;
import ro.activemall.photoxserver.entities.ProductPrice;
import ro.activemall.photoxserver.entities.RawMaterial;
import ro.activemall.photoxserver.entities.RawMaterialGroup;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

public class ProductJSON {

	private Long productId;

	private double totalBasePrice = 0;

	private String name;

	private String description;
	
	@JsonInclude(Include.NON_NULL)
	private List<GroupedSelectorsJSON> groupedSelectors;
	
	private String extraData;
	
	private boolean isComplex;
	
	@JsonIgnore
	private Map<Long, SelectorsJSON> internalSelectorsMapping = new HashMap<Long, SelectorsJSON>();
	//​$) add iscomplex,minquant, refprodid,refmatid to ProductJSON or deliver two collections
	@JsonIgnore
	private void processSelectors(Set<RawMaterialGroup> selectorGroups, Product product){
		for (RawMaterialGroup group : selectorGroups){
			if (group.getIsSelector()){
				GroupedSelectorsJSON groupedSelector = new GroupedSelectorsJSON(group, product.getMinQuantity());				
				if (group.getRawMaterials() != null){
					for (RawMaterial material : group.getRawMaterials()){
						SelectorsJSON aSelector = new SelectorsJSON(material);						
						internalSelectorsMapping.put(material.getId(), aSelector);
						groupedSelector.getSelectors().add(aSelector);
					}
				}
				groupedSelectors.add(groupedSelector);
			}else{
				if (group.getRawMaterials() != null){
					for (RawMaterial material : group.getRawMaterials()){
						//convert percents of lostRatio into effective ratio
						totalBasePrice += product.getMinQuantity() * ((product.getRatio()  + ((product.getRatio() * product.getLostRatio()) / 100)) * material.getPricePerProductionUnit());
					}
				}
			}
		}
	}

	public ProductJSON(Product product){
		try{
			this.productId = product.getId();
			this.name = product.getName();
			this.description = product.getDescription();
			this.isComplex = product.getIsComplex();
			this.extraData = product.getExtraData();
			this.groupedSelectors = new ArrayList<GroupedSelectorsJSON>();
			if (product.getIsComplex()){
				if (product.getChildren() != null){
					for (Product child : product.getChildren()){
						if (child.getSelectors() != null){						
							processSelectors(child.getSelectors(), child);		
						}						
					}
				}
			}else{
				if (product.getSelectors() != null){
					processSelectors(product.getSelectors(), product);
				}				
			}
			if (product.getPrices() != null){
				for (ProductPrice price : product.getPrices()){
					internalSelectorsMapping.get(price.getMaterialId()).setPricePerUnit(price.getSellingPrice());
				}
			}
		}catch(Exception ex){
			System.out.println("ERROR");
		}
	}
	@JsonInclude(Include.NON_EMPTY)
	public List<GroupedSelectorsJSON> getGroupedSelectors() {
		return groupedSelectors;
	}
	@JsonInclude(Include.NON_EMPTY)
	public void setGroupedSelectors(List<GroupedSelectorsJSON> groupedSelectors) {
		this.groupedSelectors = groupedSelectors;
	}

	public double getTotalBasePrice() {
		return totalBasePrice;
	}

	public void setTotalBasePrice(double basePrice) {
		this.totalBasePrice = basePrice;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Long getProductId() {
		return productId;
	}

	public void setProductId(Long productId) {
		this.productId = productId;
	}

	public String getExtraData() {
		return extraData;
	}

	public void setExtraData(String extraData) {
		this.extraData = extraData;
	}

	public boolean isComplex() {
		return isComplex;
	}

	public void setComplex(boolean isComplex) {
		this.isComplex = isComplex;
	}

	public class GroupedSelectorsJSON{
		
		private Long groupId;
		
		private int requiredQuantity;
		
		private int selectorSequence = 0;
		
		@JsonInclude(Include.NON_EMPTY)
		private List<SelectorsJSON> selectors;
		
		public GroupedSelectorsJSON(RawMaterialGroup group, int productMinQuantity){
			this.groupId = group.getId();
			this.selectorSequence = group.getSelectorSequence();
			this.requiredQuantity = productMinQuantity;
			this.selectors = new ArrayList<SelectorsJSON>();
		}
		
		@JsonInclude(Include.NON_EMPTY)
		public List<SelectorsJSON> getSelectors() {
			return selectors;
		}
		
		@JsonInclude(Include.NON_EMPTY)
		public void setSelectors(List<SelectorsJSON> selectors) {
			this.selectors = selectors;
		}
		
		public int getSelectorSequence() {
			return selectorSequence;
		}
		
		public void setSelectorSequence(int selectorSequence) {
			this.selectorSequence = selectorSequence;
		}
		
		public Long getParentId() {
			return groupId;
		}

		public void setParentId(Long parentId) {
			this.groupId = parentId;
		}

		public int getRequiredQuantity() {
			return requiredQuantity;
		}

		public void setRequiredQuantity(int requiredQuantity) {
			this.requiredQuantity = requiredQuantity;
		}

	}
	
	public class SelectorsJSON{
		
		private Long id;		
		private String name;
		private String description;
		private double pricePerUnit;

		public SelectorsJSON(RawMaterial material){			
			this.id = material.getId();					
			this.name = material.getName();
			this.description = material.getDescription();			
		}		

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getDescription() {
			return description;
		}

		public void setDescription(String description) {
			this.description = description;
		}

		public double getPricePerUnit() {
			return pricePerUnit;
		}

		public void setPricePerUnit(double price) {
			this.pricePerUnit = price;
		}

		public Long getMaterialId() {
			return id;
		}

		public void setMaterialId(Long materialId) {
			this.id = materialId;
		}

	}
}
