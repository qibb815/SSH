package com.springmvc.model;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

@Entity
public class Tree {

	private int id;
	
	private String name;
	
	private Set<Tree> children = new HashSet<Tree>();
	
	private Set<Tree> parent = new HashSet<Tree>();

	@Id
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@OneToMany(cascade=CascadeType.ALL,mappedBy="parent",fetch=FetchType.EAGER)
	public Set<Tree> getChildren() {
		return children;
	}

	public void setChildren(Set<Tree> children) {
		this.children = children;
	}

	@ManyToOne
	public Set<Tree> getParent() {
		return parent;
	}

	public void setParent(Set<Tree> parent) {
		this.parent = parent;
	}
}
