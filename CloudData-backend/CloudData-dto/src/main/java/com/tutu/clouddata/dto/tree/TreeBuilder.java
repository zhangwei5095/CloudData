package com.tutu.clouddata.dto.tree;

import java.util.ArrayList;
import java.util.List;

import org.springframework.util.StringUtils;


public class TreeBuilder<T> {
	List<TreeEntity<T>> treeEntitys = new ArrayList<TreeEntity<T>>();

	public TreeBuilder(List<TreeEntity<T>> treeEntitys) {
		super();
		this.treeEntitys = treeEntitys;
	}

	/**
	 * 构建树形结构
	 * 
	 * @return
	 */
	public List<TreeEntity<T>> buildTree() {
		List<TreeEntity<T>> treeTreeEntitys = new ArrayList<TreeEntity<T>>();
		List<TreeEntity<T>> rootTreeEntitys = getRootTreeEntitys();
		for (TreeEntity<T> rootTreeEntity : rootTreeEntitys) {
			buildChildTreeEntitys(rootTreeEntity);
			treeTreeEntitys.add(rootTreeEntity);
		}
		return treeTreeEntitys;
	}

	/**
	 * 递归子节点
	 * 
	 * @param TreeEntity
	 */
	public void buildChildTreeEntitys(TreeEntity<T> treeEntity) {
		List<TreeEntity<T>> children = getChildTreeEntitys(treeEntity);
		if (!children.isEmpty()) {
			for (TreeEntity<T> child : children) {
				buildChildTreeEntitys(child);
			}
			treeEntity.setChildren(children);
		}
	}

	/**
	 * 获取父节点下所有的子节点
	 * 
	 * @param TreeEntitys
	 * @param pTreeEntity
	 * @return
	 */
	public List<TreeEntity<T>> getChildTreeEntitys(TreeEntity<T> pTreeEntity) {
		List<TreeEntity<T>> childTreeEntitys = new ArrayList<TreeEntity<T>>();
		for (TreeEntity<T> n : treeEntitys) {
			if (pTreeEntity.getId().equals(n.getPid())) {
				childTreeEntitys.add(n);
			}
		}
		return childTreeEntitys;
	}

	/**
	 * 判断是否为根节点
	 * 
	 * @param TreeEntitys
	 * @param inTreeEntity
	 * @return
	 */
	public boolean rootTreeEntity(TreeEntity<T> treeEntity) {
		boolean isRootTreeEntity = true;
		for (TreeEntity<T> n : treeEntitys) {
			if (!StringUtils.isEmpty(treeEntity.getPid()) && treeEntity.getPid().equals(n.getId())) {
				isRootTreeEntity = false;
				break;
			}
		}
		return isRootTreeEntity;
	}

	/**
	 * 获取集合中所有的根节点
	 * 
	 * @param TreeEntitys
	 * @return
	 */
	public List<TreeEntity<T>> getRootTreeEntitys() {
		List<TreeEntity<T>> rootTreeEntitys = new ArrayList<TreeEntity<T>>();
		for (TreeEntity<T> n : treeEntitys) {
			if (rootTreeEntity(n)) {
				rootTreeEntitys.add(n);
			}
		}
		return rootTreeEntitys;
	}
}
