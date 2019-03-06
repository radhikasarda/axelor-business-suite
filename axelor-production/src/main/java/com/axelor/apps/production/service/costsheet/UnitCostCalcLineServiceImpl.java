/*
 * Axelor Business Solutions
 *
 * Copyright (C) 2019 Axelor (<http://axelor.com>).
 *
 * This program is free software: you can redistribute it and/or  modify
 * it under the terms of the GNU Affero General Public License, version 3,
 * as published by the Free Software Foundation.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.axelor.apps.production.service.costsheet;

import com.axelor.apps.base.db.Product;
import com.axelor.apps.base.db.repo.ProductRepository;
import com.axelor.apps.production.db.CostSheet;
import com.axelor.apps.production.db.UnitCostCalcLine;
import com.axelor.apps.production.db.UnitCostCalculation;
import com.axelor.apps.production.db.repo.UnitCostCalcLineRepository;
import com.google.inject.Inject;
import java.lang.invoke.MethodHandles;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UnitCostCalcLineServiceImpl implements UnitCostCalcLineService {

  private final Logger log = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

  protected ProductRepository productRepository;
  protected UnitCostCalcLineRepository unitCostCalcLineRepository;

  @Inject
  public UnitCostCalcLineServiceImpl(UnitCostCalcLineRepository unitCostCalcLineRepository) {
    this.unitCostCalcLineRepository = unitCostCalcLineRepository;
  }

  public UnitCostCalcLine createUnitCostCalcLine(
      Product product, int maxLevel, CostSheet costSheet) {

    UnitCostCalcLine unitCostCalcLine = new UnitCostCalcLine();
    unitCostCalcLine.setProduct(product);
    unitCostCalcLine.setPreviousCost(product.getCostPrice());
    unitCostCalcLine.setCostSheet(costSheet);
    unitCostCalcLine.setComputedCost(costSheet.getCostPrice());
    unitCostCalcLine.setCostToApply(costSheet.getCostPrice());
    unitCostCalcLine.setMaxLevel(maxLevel);

    return unitCostCalcLine;
  }

  public UnitCostCalcLine getUnitCostCalcLine(
      UnitCostCalculation unitCostCalculation, Product product) {

    return unitCostCalcLineRepository
        .all()
        .filter("self.unitCostCalculation = ?1 AND self.product = ?2", unitCostCalculation, product)
        .fetchOne();
  }
}
