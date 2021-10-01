//package com.tjr.perval.widgets.quotitian;
//
//import java.util.ArrayList;
//import java.util.Collections;
//import java.util.Comparator;
//import java.util.HashMap;
//import java.util.Iterator;
//import java.util.List;
//import java.util.Map;
//import java.util.regex.Matcher;
//import java.util.regex.Pattern;
//
//import com.cropyme.common.CommonConst;
//
//public class ParserShareTimeUtils {
//	/**
//	 * 解析指数分时线
//	 *
//	 * @param describes
//	 * @param minMap
//	 *            对分时线
//	 * @param amtmap
//	 *            对成交额
//	 * @param minMapXY
//	 *            保存当前xy坐标的值
//	 * @param spanX
//	 *            当前x坐标的跨度值
//	 * @param spanY
//	 *            当前y坐标的跨度值
//	 * @param maxValue
//	 *            当前最大值
//	 */
//	@Deprecated
//	public static void parserMinuteDataHisForIndex(String describes, Map<Integer, Float> minMap, Map<Integer, Float> amtMap, Map<Float, Float> minMapXY, float spanX, float spanY, float maxValue) {
//		try {
//			// 当前价1,成交量2,成交金额3,成时间(时分秒)4;
//			final String regex4 = "([0-9]*[.]??[0-9]*),([0-9]*[.]??[0-9]*),([0-9]*[.]??[0-9]*),([0-9]+)"; // 总
//			final Pattern pa4 = Pattern.compile(regex4, Pattern.DOTALL); // 总
//			final Matcher ma4 = pa4.matcher(describes);
//			int time;
//			float zjcj, cjje;
//			while (ma4.find()) {
//				if (ma4.groupCount() == 4) {
//					if (ma4.group(4).matches("[0-9]+$")) {
//						time = Integer.parseInt(ma4.group(4));
//						if (time >= 930) {
//							if (minMap != null && ma4.group(1).matches(CommonConst.FLOATMATCHES)) {
//								zjcj = (float) Double.parseDouble(ma4.group(1));
//								minMap.put(time, zjcj);
//								if (minMapXY != null) {
//									minMapXY.put(StockChartUtil.getCharPixelX(spanX, ma4.group(4)), StockChartUtil.getCharPixelY(maxValue, spanY, zjcj));
//								}
//							}
//							if (amtMap != null && ma4.group(3).matches(CommonConst.FLOATMATCHES)) {
//								cjje = (float) Double.parseDouble(ma4.group(3));
//								amtMap.put(time, cjje / 10000);
//							}
//						}
//					}
//				}
//			}
//		} catch (Exception e) {
//			// TODO: handle exception
//		} catch (OutOfMemoryError o) {
//			// TODO: handle exception
//			o.printStackTrace();
//		}
//	}
//
//	/**
//	 * 解析首页指数分时线
//	 */
//	@Deprecated
//	public static void parserMinuteDataHisForHome(String describes, Map<Integer, Float> minMap, Map<Float, Float> minMapXY, float spanX, float spanY, float maxValue) {
//		try {
//			// 当前价1,成时间(时分秒)2;
//			final String regex2 = "([0-9]*[.]??[0-9]*),([0-9]+)"; // 总
//			final Pattern pa2 = Pattern.compile(regex2, Pattern.DOTALL); // 总
//			final Matcher ma2 = pa2.matcher(describes);
//			int time;
//			float zjcj;
//			while (ma2.find()) {
//				if (ma2.groupCount() == 2) {
//					if (ma2.group(2).matches("[0-9]+$")) {
//						time = Integer.parseInt(ma2.group(2));
//						if (time >= 930) {
//							if (minMap != null && ma2.group(1).matches(CommonConst.FLOATMATCHES)) {
//								zjcj = (float) Double.parseDouble(ma2.group(1));
//								minMap.put(time, zjcj);
//								if (minMapXY != null) {
//									minMapXY.put(StockChartUtil.getCharPixelX(spanX, ma2.group(2)), StockChartUtil.getCharPixelY(maxValue, spanY, zjcj));
//								}
//							}
//						}
//					}
//				}
//			}
//		} catch (Exception e) {
//			// TODO: handle exception
//		} catch (OutOfMemoryError o) {
//			// TODO: handle exception
//			o.printStackTrace();
//		}
//	}
//
//	/**
//	 * 指数 解析均线
//	 *
//	 * @param describes
//	 * @param maMap
//	 *            对均线线
//	 * @param maMapXY
//	 *            保存当前xy坐标的值
//	 * @param spanX
//	 *            当前x坐标的跨度值
//	 * @param spanY
//	 *            当前y坐标的跨度值
//	 * @param maxValue
//	 *            当前最大值
//	 */
//	@Deprecated
//	public static void parserMaResultHisForIndex(String describes, Map<Integer, Float> maMap, Map<Float, Float> maMapXY, float spanX, float spanY, float maxValue) {
//		try {
//			// 当前价1,成时间(时分秒)4;
//			final String regex2 = "([0-9]*[.]??[0-9]*),([0-9]+)"; // 总
//			final Pattern pa2 = Pattern.compile(regex2, Pattern.DOTALL); // 总
//			final Matcher ma2 = pa2.matcher(describes);
//			int time;
//			float zjcj;
//			while (ma2.find()) {
//				if (ma2.groupCount() == 2) {
//					if (maMap != null && ma2.group(2).matches("[0-9]+$")) {
//						time = Integer.parseInt(ma2.group(2));
//						if (time >= 930) {
//							if (ma2.group(1).matches(CommonConst.FLOATMATCHES)) {
//								zjcj = (float) Double.parseDouble(ma2.group(1));
//								maMap.put(time, zjcj);
//								if (maMapXY != null) maMapXY.put(StockChartUtil.getCharPixelX(spanX, ma2.group(2)), StockChartUtil.getCharPixelY(maxValue, spanY, zjcj));
//							}
//						}
//					}
//				}
//			}
//		} catch (Exception e) {
//			// TODO: handle exception
//		} catch (OutOfMemoryError o) {
//			// TODO: handle exception
//			o.printStackTrace();
//		}
//	}
//
//	/**
//	 * 重新解析分时线XY坐标值
//	 *
//	 * @param describes
//	 * @param minMapXY
//	 *            保存当前xy坐标的值
//	 * @param spanX
//	 *            当前x坐标的跨度值
//	 * @param spanY
//	 *            当前y坐标的跨度值
//	 * @param maxValue
//	 *            当前最大值
//	 */
//	@Deprecated
//	public static void parserAgainDataHisXY(Map<Integer, Float> map, Map<Float, Float> mapXY, float spanX, float spanY, float maxValue) {
//		try {
//			if (mapXY != null && map != null) {
//				Iterator<Map.Entry<Integer, Float>> iter = map.entrySet().iterator();
//				while (iter.hasNext()) {
//					Map.Entry<Integer, Float> entry = iter.next();
//					mapXY.put(StockChartUtil.getCharPixelX(spanX, entry.getKey().toString()), StockChartUtil.getCharPixelY(maxValue, spanY, entry.getValue()));
//				}
//			}
//		} catch (Exception e) {
//			// TODO: handle exception
//		} catch (OutOfMemoryError o) {
//			// TODO: handle exception
//			o.printStackTrace();
//		}
//	}
//
//	/**
//	 * 重新解析成交量XY坐标值
//	 *
//	 * @param describes
//	 * @param minMapXY
//	 *            保存当前xy坐标的值
//	 * @param spanX
//	 *            当前x坐标的跨度值
//	 * @param volumeSpanY
//	 *            当前y坐标的跨度值
//	 * @param volumeMax
//	 *            当前最大值
//	 */
//	@Deprecated
//	public static void parserAgainVolDataHisXY(Map<Integer, Float> map, Map<Float, Float> mapXY, float spanX, float spanAmtOrVolY, float maxAmtOrVolValue) {
//		try {
//			if (map != null && mapXY != null) {
//				List<Map.Entry<Integer, Float>> infoIdsVol = new ArrayList<Map.Entry<Integer, Float>>(map.entrySet());
//				Collections.sort(infoIdsVol, new Comparator<Map.Entry<Integer, Float>>() {
//					public int compare(Map.Entry<Integer, Float> o1, Map.Entry<Integer, Float> o2) {
//						return (o1.getKey() - o2.getKey());
//					}
//				});
//				Iterator<Map.Entry<Integer, Float>> iter = infoIdsVol.iterator();
//				float preVolY = 0.0f, volY = 0.0f;
//				while (iter.hasNext()) {
//					Map.Entry<Integer, Float> entry = iter.next();
//					volY = StockChartUtil.getCharVolumeY(maxAmtOrVolValue, spanAmtOrVolY, entry.getValue() - preVolY);
//					if (volY != 0) mapXY.put(StockChartUtil.getCharPixelX(spanX, String.valueOf(entry.getKey())), volY);
//					preVolY = entry.getValue();
//				}
//			}
//		} catch (Exception e) {
//			// TODO: handle exception
//		} catch (OutOfMemoryError o) {
//			// TODO: handle exception
//			o.printStackTrace();
//		}
//	}
//
//	/**
//	 *
//	 * 解析分时线及黄线结果
//	 *
//	 * @param describes
//	 * @param minMap
//	 *            分时线
//	 * @param maMap
//	 *            黄线
//	 * @param volMap
//	 *            成交量
//	 * @param minMapXY
//	 *            计算分时线XY坐标值
//	 * @param maMapXY
//	 *            计算黄线线XY坐标值
//	 * @param volMapXY
//	 *            计算成交量线XY坐标值
//	 * @param spanX
//	 *            当前x坐标的跨度值
//	 * @param spanY
//	 *            当前y坐标的跨度值
//	 * @param maxValue
//	 *            当前最大值
//	 */
//	@Deprecated
//	public static void parserMinuteDataHisAndMa(String describes, Map<Integer, Float> minMap, Map<Integer, Float> maMap, Map<Integer, Float> volMap, Map<Float, Float> minMapXY, Map<Float, Float> maMapXY, float spanX, float spanY, float maxValue) {
//		try {
//			// 当前价1,成交量2,成交金额3,成时间(时分秒)4;
//			final String regex4 = "([0-9]*[.]??[0-9]*),([0-9]*[.]??[0-9]*),([0-9]*[.]??[0-9]*),([0-9]+)"; // 总
//			final Pattern pa4 = Pattern.compile(regex4, Pattern.DOTALL); // 总
//			final Matcher ma4 = pa4.matcher(describes);
//			int time;
//			float zjcj, cjsl, cjje;
//			while (ma4.find()) {
//				if (ma4.groupCount() == 4) {
//					if (ma4.group(4).matches("[0-9]+$")) {
//						time = Integer.parseInt(ma4.group(4));
//						if (time >= 930) {
//							if (ma4.group(1).matches(CommonConst.FLOATMATCHES)) {
//								zjcj = (float) Double.parseDouble(ma4.group(1));
//								if (minMap != null) {
//									minMap.put(time, zjcj);
//									if (minMapXY != null) minMapXY.put(StockChartUtil.getCharPixelX(spanX, ma4.group(4)), StockChartUtil.getCharPixelY(maxValue, spanY, zjcj));
//								}
//								if (ma4.group(2).matches(CommonConst.FLOATMATCHES) && ma4.group(3).matches(CommonConst.FLOATMATCHES)) {
//									cjsl = (float) Double.parseDouble(ma4.group(2));
//									cjje = (float) Double.parseDouble(ma4.group(3));
//									if (maMap != null && cjsl != 0.0) {
//										maMap.put(time, cjje / cjsl);
//										if (maMapXY != null) maMapXY.put(StockChartUtil.getCharPixelX(spanX, ma4.group(4)), StockChartUtil.getCharPixelY(maxValue, spanY, maMap.get(time)));
//									}
//									if (volMap != null) volMap.put(time, cjsl / 100);
//								}
//							}
//						}
//					}
//				}
//			}
//		} catch (Exception e) {
//			// TODO: handle exception
//			e.printStackTrace();
//		} catch (OutOfMemoryError o) {
//			o.printStackTrace();
//		}
//	}
//
//	/**
//	 *
//	 * 解析分时线及黄线结果
//	 *
//	 * @param describes
//	 * @param minMap
//	 *            分时线
//	 * @param maMap
//	 *            黄线
//	 * @param volMap
//	 *            成交量
//	 */
//	@Deprecated
//	public static void parserMinuteDataHisAndMaAndVol(String describes, Map<Integer, Float> minMap, Map<Integer, Float> maMap, Map<Integer, Float> volMap) {
//		try {
//			// 当前价1,成交量2,成交金额3,成时间(时分秒)4;
//			final String regex4 = "([0-9]*[.]??[0-9]*),([0-9]*[.]??[0-9]*),([0-9]*[.]??[0-9]*),([0-9]+)"; // 总
//			final Pattern pa4 = Pattern.compile(regex4, Pattern.DOTALL); // 总
//			final Matcher ma4 = pa4.matcher(describes);
//			int time;
//			float zjcj, cjsl, cjje;
//			while (ma4.find()) {
//				if (ma4.groupCount() == 4) {
//					if (ma4.group(4).matches(CommonConst.INTMATCHES)) {
//						time = Integer.parseInt(ma4.group(4));
//						if (time >= 930) {
//							if (ma4.group(1).matches(CommonConst.FLOATMATCHES)) {
//								zjcj = (float) Double.parseDouble(ma4.group(1));
//								if (minMap != null) minMap.put(time, zjcj);
//								if (ma4.group(2).matches(CommonConst.FLOATMATCHES) && ma4.group(3).matches(CommonConst.FLOATMATCHES)) {
//									cjsl = (float) Double.parseDouble(ma4.group(2));
//									cjje = (float) Double.parseDouble(ma4.group(3));
//									if (maMap != null && cjsl != 0.0) maMap.put(time, cjje / cjsl);
//									if (volMap != null) volMap.put(time, cjsl / 100);
//								}
//							}
//						}
//					}
//				}
//			}
//		} catch (Exception e) {
//			// TODO: handle exception
//			e.printStackTrace();
//		} catch (OutOfMemoryError o) {
//			o.printStackTrace();
//		}
//	}
//
//	/**
//	 * 去掉数据并设置数据集
//	 * @param spanX
//	 * @param bakList
//	 * @param minTimeList
//	 */
//	public static void duplicateRemovalSetList(float spanX,List<MinTimeEntity> bakList,final List<MinTimeEntity> minTimeList){
//		if(bakList == null)return;
//		Collections.sort(bakList, new Comparator<MinTimeEntity>() {
//			@Override
//			public int compare(MinTimeEntity s1, MinTimeEntity s2) {
//				return s1.time - s2.time;
//			}
//		});
//		Map<Float, MinTimeEntity> map = new HashMap<Float, MinTimeEntity>();
//		for (MinTimeEntity entity : bakList) {
//			map.put(StockChartUtil.getCharPixelX(spanX, String.valueOf(entity.time)), entity);
//		}
//		for (MinTimeEntity entity : map.values()) {
//			minTimeList.add(entity);
//		}
//		bakList.clear();
//		bakList = null;
//		map.clear();
//		map = null;
//	}
//
//	/**
//	 * 解析分时线结果集放到list数组里
//	 *
//	 * @param describes
//	 * @param minTimeList
//	 */
//	public static List<MinTimeEntity> parserMinuteDataHisSetList(final String describes) throws Exception {
//		// 当前价1,成交量2,成交金额3,成时间(时分秒)4;
//		final String regex4 = "([0-9]*[.]??[0-9]*),([0-9]*[.]??[0-9]*),([0-9]*[.]??[0-9]*),([0-9]+)"; // 总
//		final Pattern pa4 = Pattern.compile(regex4, Pattern.DOTALL); // 总
//		final Matcher ma4 = pa4.matcher(describes);
//		int time;
//		float zjcj, cjsl, cjje;
//		MinTimeEntity minTimeEntity;
//		List<MinTimeEntity> bakList = new ArrayList<MinTimeEntity>();
//		while (ma4.find()) {
//			if (ma4.groupCount() == 4) {
//				if (ma4.group(4).matches(CommonConst.INTMATCHES)) {
//					time = Integer.parseInt(ma4.group(4));
//					if (time >= 930) {
//						if (ma4.group(1).matches(CommonConst.FLOATMATCHES)) {
//							minTimeEntity = new MinTimeEntity();
//							minTimeEntity.time = time;
//							zjcj = (float) Double.parseDouble(ma4.group(1));
//							minTimeEntity.zjcj = zjcj;
//							if (ma4.group(2).matches(CommonConst.FLOATMATCHES) && ma4.group(3).matches(CommonConst.FLOATMATCHES)) {
//								cjsl = (float) Double.parseDouble(ma4.group(2));
//								cjje = (float) Double.parseDouble(ma4.group(3));
//								if(cjsl != 0)minTimeEntity.ma = cjje / cjsl;
//								minTimeEntity.vol = cjsl / 100;
//								bakList.add(minTimeEntity);
//							}
//						}
//					}
//				}
//			}
//		}
//		return bakList;
//	}
//
//	/**
//	 * 设置指数解析
//	 *
//	 * @param describes
//	 * @param spanX
//	 * @param minTimeList
//	 */
//	public static List<MinTimeEntity> parserIndexMinuteDataHisSetList(final String describes) throws Exception {
//		// 当前价1,成交量2,成交金额3,成时间(时分秒)4;
//		final String regex4 = "([0-9]*[.]??[0-9]*),([0-9]*[.]??[0-9]*),([0-9]*[.]??[0-9]*),([0-9]+)"; // 总
//		final Pattern pa4 = Pattern.compile(regex4, Pattern.DOTALL); // 总
//		final Matcher ma4 = pa4.matcher(describes);
//		int time;
//		float zjcj, cjje;
//		MinTimeEntity minTimeEntity;
//		List<MinTimeEntity> bakList = new ArrayList<MinTimeEntity>();
//		while (ma4.find()) {
//			if (ma4.groupCount() == 4) {
//				if (ma4.group(4).matches(CommonConst.INTMATCHES)) {
//					time = Integer.parseInt(ma4.group(4));
//					if (time >= 930) {
//						if (ma4.group(1).matches(CommonConst.FLOATMATCHES)) {
//							minTimeEntity = new MinTimeEntity();
//							minTimeEntity.time = time;
//							zjcj = (float) Double.parseDouble(ma4.group(1));
//							minTimeEntity.zjcj = zjcj;
//							if (ma4.group(2).matches(CommonConst.FLOATMATCHES) && ma4.group(3).matches(CommonConst.FLOATMATCHES)) {
//								cjje = (float) Double.parseDouble(ma4.group(3));
//								minTimeEntity.vol = cjje / 10000;
//								bakList.add(minTimeEntity);
//							}
//						}
//					}
//				}
//			}
//		}
//		return bakList;
//	}
//
//	/**
//	 * 指数黄线解析
//	 *
//	 * @param describes
//	 * @param spanX
//	 * @param maTimeList
//	 */
//	public static List<MinTimeEntity> parserMaResultHisIndexMinuteDataHisSetList(final String describes) throws Exception{
//		// 当前价1,成交量2,成交金额3,成时间(时分秒)4;
//		final String regex2 = "([0-9]*[.]??[0-9]*),([0-9]+)"; // 总
//		final Pattern pa2 = Pattern.compile(regex2, Pattern.DOTALL); // 总
//		final Matcher ma2 = pa2.matcher(describes);
//		int time;
//		MinTimeEntity minTimeEntity;
//		List<MinTimeEntity> bakList = new ArrayList<MinTimeEntity>();
//		while (ma2.find()) {
//			if (ma2.groupCount() == 2) {
//				if (ma2.group(2).matches(CommonConst.INTMATCHES)) {
//					time = Integer.parseInt(ma2.group(2));
//					if (time >= 930) {
//						if (ma2.group(1).matches(CommonConst.FLOATMATCHES)) {
//							minTimeEntity = new MinTimeEntity();
//							minTimeEntity.time = time;
//							minTimeEntity.ma = (float) Double.parseDouble(ma2.group(1));
//							bakList.add(minTimeEntity);
//						}
//					}
//				}
//			}
//		}
//		return bakList;
//	}
//
//	/**
//	 * 解析首页指数分时线
//	 */
//	public static List<MinTimeEntity> parserMinuteDataHisForHomeSetList(final String describes) throws Exception{
//		// 当前价1,成时间(时分秒)2;
//		final String regex2 = "([0-9]*[.]??[0-9]*),([0-9]+)"; // 总
//		final Pattern pa2 = Pattern.compile(regex2, Pattern.DOTALL); // 总
//		final Matcher ma2 = pa2.matcher(describes);
//		int time;
//		MinTimeEntity minTimeEntity;
//		List<MinTimeEntity> bakList = new ArrayList<MinTimeEntity>();
//		while (ma2.find()) {
//			if (ma2.groupCount() == 2) {
//				if (ma2.group(2).matches(CommonConst.INTMATCHES)) {
//					time = Integer.parseInt(ma2.group(2));
//					if (time >= 930) {
//						if (ma2.group(1).matches(CommonConst.FLOATMATCHES)) {
//							minTimeEntity = new MinTimeEntity();
//							minTimeEntity.time = time;
//							minTimeEntity.zjcj = (float) Double.parseDouble(ma2.group(1));
//							bakList.add(minTimeEntity);
//						}
//					}
//				}
//			}
//		}
//		return bakList;
//	}
//}
