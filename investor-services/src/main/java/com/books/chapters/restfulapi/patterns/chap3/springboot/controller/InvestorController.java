package com.books.chapters.restfulapi.patterns.chap3.springboot.controller;

import java.net.URI;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.books.chapters.restfulapi.patterns.chap3.springboot.models.Investor;
import com.books.chapters.restfulapi.patterns.chap3.springboot.models.Stock;
import com.books.chapters.restfulapi.patterns.chap3.springboot.service.InvestorService;

@RestController
public class InvestorController {

	private static final String ID = "/{id}";
	@Autowired
	private InvestorService investorService;

	@GetMapping("/investors")
	public List<Investor> fetchAllInvestors() {
		return investorService.fetchAllInvestors();
	}

	@GetMapping("/investors/{investorId}")
	public Investor fetchInvestorById(@PathVariable String investorId) {
		return investorService.fetchInvestorById(investorId);
	}

	@GetMapping("/investors/{investorId}/stocks")
	public List<Stock> fetchStocksByInvestorId(@PathVariable String investorId) {
		return investorService.fetchStocksByInvestorId(investorId);
	}

	@GetMapping("/investors/{investorId}/stocks/{symbol}")
	public Stock fetchAStockByInvestorIdAndStockId(@PathVariable String investorId, @PathVariable String symbol) {
		return investorService.fetchSingleStockByInvestorIdAndStockSymbol(investorId, symbol);
	}

	@PostMapping("/investors/{investorId}/stocks")
	public ResponseEntity<Void> addNewStockToTheInvestorPortfolio(@PathVariable String investorId,
			@RequestBody Stock newStock) {
		Stock insertedStock = investorService.addNewStockToTheInvestorPortfolio(investorId, newStock);
		if (insertedStock == null) {
			return ResponseEntity.noContent().build();
		}

		URI location = ServletUriComponentsBuilder.fromCurrentRequest().path(ID)
				.buildAndExpand(insertedStock.getSymbol()).toUri();
		return ResponseEntity.created(location).build();
	}

	@PutMapping("/investors/{investorId}/stocks")
	public ResponseEntity<Void> updateAStockOfTheInvestorPortfolio(@PathVariable String investorId,
			@RequestBody Stock stockTobeUpdated) {
		Stock updatedStock = investorService.updateAStockByInvestorIdAndStock(investorId, stockTobeUpdated);
		if (updatedStock == null) {
			return ResponseEntity.noContent().build();
		}
		URI location = ServletUriComponentsBuilder.fromCurrentRequest().path(ID)
				.buildAndExpand(updatedStock.getSymbol()).toUri();
		return ResponseEntity.created(location).build();
	}
	
	@PatchMapping("/investors/{investorId}/stocks/{symbol}")
	public ResponseEntity<Void> updateAStockOfTheInvestorPortfolio(@PathVariable String investorId,
			@PathVariable String symbol, @RequestBody Stock stockTobeUpdated) {
		Stock updatedStock = investorService.updateAStockByInvestorIdAndStock(investorId, symbol, stockTobeUpdated);
		if (updatedStock == null) {
			return ResponseEntity.noContent().build();
		}
		URI location = ServletUriComponentsBuilder.fromCurrentRequest().path(ID)
				.buildAndExpand(updatedStock.getSymbol()).toUri();
		return ResponseEntity.created(location).build();
	}

	@DeleteMapping("/investors/{investorId}/stocks/{symbol}")
	public ResponseEntity<Void> deleteAStockFromTheInvestorPortfolio(@PathVariable String investorId,
			@PathVariable String symbol) {
		if (investorService.deleteStockFromTheInvestorPortfolio(investorId, symbol)) {
			return ResponseEntity.noContent().build();
		}
		return ResponseEntity.ok(null);
	}
}