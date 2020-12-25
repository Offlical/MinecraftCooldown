package dev.offlical.mccd;

import java.util.UUID;

public class Cooldowns {

	public Long moveHorzCD = System.currentTimeMillis();
	public Long moveUpCD = System.currentTimeMillis();
	public Long breakCD = System.currentTimeMillis();
	public Long placeCD = System.currentTimeMillis();
	public Long regenCD = System.currentTimeMillis();
	public Long bucketCD = System.currentTimeMillis();
	public Long bucketEmptyCD = System.currentTimeMillis();
	public Long damageCD = System.currentTimeMillis();
	public Long bowShotCD = System.currentTimeMillis();
	public Long interactLeftCD = System.currentTimeMillis();
	public Long interactRightCD = System.currentTimeMillis();
	public Long eggThrowCD = System.currentTimeMillis();
	public Long craftCD = System.currentTimeMillis();
	public Long dropItemCD = System.currentTimeMillis();
	public Long pickupItemCD = System.currentTimeMillis();
	public Long igniteCD = System.currentTimeMillis();
	public Long blockdmgCD = System.currentTimeMillis();
	public Long consumeCD = System.currentTimeMillis();
	public UUID playeruuid;
	
	public Cooldowns(UUID uuid) {
		this.playeruuid = uuid;
	}
	
	public boolean isCooldownOver(String s) {
		if(Main.isCd() == false) return true;

		
		switch(CDType.valueOf(s)) {
		
			case REGENHP:
				return this.regenCD <= System.currentTimeMillis();
			case DAMAGE_ENTITY:
				return this.damageCD <= System.currentTimeMillis();
			case PLACE:
				return this.placeCD <= System.currentTimeMillis();
			case BREAK:
				return this.breakCD <= System.currentTimeMillis();
			case MOVE_HORZ:
				return this.moveHorzCD <= System.currentTimeMillis();
			case MOVE_UP:
				return this.moveUpCD <= System.currentTimeMillis();
			case BOW_SHOT:
				return this.bowShotCD <= System.currentTimeMillis();
			case EGG_THROW:
				return this.eggThrowCD <= System.currentTimeMillis();
			case INTERACT_LEFT:
				return this.interactLeftCD <= System.currentTimeMillis();
			case INTERACT_RIGHT:
				return this.interactRightCD <= System.currentTimeMillis();
			case CRAFT:
				return this.craftCD <= System.currentTimeMillis();
			case BUCKET:
				return this.bucketCD <= System.currentTimeMillis();
			case BUCKET_EMPTY: 
				return this.bucketEmptyCD <= System.currentTimeMillis();
			case DROP_ITEM:
				return this.dropItemCD <= System.currentTimeMillis();
			case PICKUP_ITEM:
				return this.pickupItemCD <= System.currentTimeMillis();
			case IGNITE:
				return this.igniteCD <= System.currentTimeMillis();
			case BLOCK_DAMAGE:
				return this.blockdmgCD <= System.currentTimeMillis();
			case CONSUME:
				return this.consumeCD <= System.currentTimeMillis();
		}
		return false;
	}

	
	public Long getConsumeCD() {
		return consumeCD;
	}

	public void setConsumeCD(Long consumeCD) {
		this.consumeCD = consumeCD;
	}

	public Long getBlockdmgCD() {
		return blockdmgCD;
	}

	public void setBlockdmgCD(Long blockdmgCD) {
		this.blockdmgCD = blockdmgCD;
	}

	public Long getIgniteCD() {
		return igniteCD;
	}

	public void setIgniteCD(Long igniteCD) {
		this.igniteCD = igniteCD;
	}

	public Long getPickupItemCD() {
		return pickupItemCD;
	}

	

	public Long getMoveHorzCD() {
		return moveHorzCD;
	}

	public Long getMoveUpCD() {
		return moveUpCD;
	}

	public Long getBreakCD() {
		return breakCD;
	}

	public Long getPlaceCD() {
		return placeCD;
	}

	public Long getRegenCD() {
		return regenCD;
	}

	public Long getBucketCD() {
		return bucketCD;
	}

	public Long getBucketEmptyCD() {
		return bucketEmptyCD;
	}

	public Long getDamageCD() {
		return damageCD;
	}

	public Long getBowShotCD() {
		return bowShotCD;
	}

	public Long getInteractLeftCD() {
		return interactLeftCD;
	}

	public Long getInteractRightCD() {
		return interactRightCD;
	}

	public Long getEggThrowCD() {
		return eggThrowCD;
	}

	public Long getCraftCD() {
		return craftCD;
	}

	public Long getDropItemCD() {
		return dropItemCD;
	}

	public UUID getPlayeruuid() {
		return playeruuid;
	}
	public void setPickupItemCD(Long pickupItemCD) {
		this.pickupItemCD = pickupItemCD;
	}
	public void setMoveHorzCD(Long moveHorzCD) {
		this.moveHorzCD = moveHorzCD;
	}

	public void setMoveUpCD(Long moveUpCD) {
		this.moveUpCD = moveUpCD;
	}

	public void setBreakCD(Long breakCD) {
		this.breakCD = breakCD;
	}

	public void setPlaceCD(Long placeCD) {
		this.placeCD = placeCD;
	}

	public void setRegenCD(Long regenCD) {
		this.regenCD = regenCD;
	}

	public void setBucketCD(Long bucketCD) {
		this.bucketCD = bucketCD;
	}

	public void setBucketEmptyCD(Long bucketEmptyCD) {
		this.bucketEmptyCD = bucketEmptyCD;
	}

	public void setDamageCD(Long damageCD) {
		this.damageCD = damageCD;
	}

	public void setBowShotCD(Long bowShotCD) {
		this.bowShotCD = bowShotCD;
	}

	public void setInteractLeftCD(Long interactLeftCD) {
		this.interactLeftCD = interactLeftCD;
	}

	public void setInteractRightCD(Long interactRightCD) {
		this.interactRightCD = interactRightCD;
	}

	public void setEggThrowCD(Long eggThrowCD) {
		this.eggThrowCD = eggThrowCD;
	}

	public void setCraftCD(Long craftCD) {
		this.craftCD = craftCD;
	}

	public void setDropItemCD(Long dropItemCD) {
		this.dropItemCD = dropItemCD;
	}

	public void setPlayeruuid(UUID playeruuid) {
		this.playeruuid = playeruuid;
	}
	
	
	
}
