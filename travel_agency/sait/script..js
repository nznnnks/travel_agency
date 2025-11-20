// Фильтры маршрутов
let filterSearch, routeGrid;

document.addEventListener('DOMContentLoaded',()=>{
  filterSearch = document.querySelector('#filterSearch');
  routeGrid = document.querySelector('#routesGrid');
window.applyFilters = function(){
  console.log('Применяем фильтры...');
  const activeType=document.querySelector('input[name="type"]:checked')?.value || '';
  const activeDiff=document.querySelector('input[name="difficulty"]:checked')?.value || '';
  const activeRegion=document.querySelector('input[name="region"]:checked')?.value || '';
  const minDays=parseInt(document.querySelector('#daysMin')?.value||'0',10);
  const maxDays=parseInt(document.querySelector('#daysMax')?.value||'999',10);
  const q=filterSearch?filterSearch.value.trim().toLowerCase():'';
  const cards=routeGrid?routeGrid.querySelectorAll('.card[data-type]'):[];
  
  console.log('Найдено карточек:', cards.length);
  console.log('Активные фильтры:', {activeType, activeDiff, activeRegion, minDays, maxDays, q});
  
  let visibleCount = 0;
  
  // Проверяем, есть ли активные фильтры
  const hasActiveFilters = activeType || activeDiff || activeRegion || (minDays > 0) || (maxDays < 999) || q;
  
  cards.forEach(card=>{
    const type=card.dataset.type;
    const diff=card.dataset.difficulty;
    const region=card.dataset.region;
    const days=parseInt(card.dataset.days,10);
    const title=card.dataset.title.toLowerCase();
    
    let ok=true;
    
    // Если ничего не выбрано → показываются все маршруты
    if (!hasActiveFilters) {
      ok = true;
    } else {
      // Если выбран один признак → фильтрация работает ТОЛЬКО по этому признаку
      // Если выбрано несколько признаков → фильтрация работает ТОЛЬКО по всем выбранным
      
      // Фильтр по типу
      if(activeType && activeType !== '') {
        ok = ok && activeType === type;
      }
      
      // Фильтр по сложности
      if(activeDiff && activeDiff !== '') {
        ok = ok && activeDiff === diff;
      }
      
      // Фильтр по региону
      if(activeRegion && activeRegion !== '') {
        ok = ok && activeRegion === region;
      }
      
      // Фильтр по длительности
      if(minDays > 0 || maxDays < 999) {
        ok = ok && days >= minDays && days <= maxDays;
      }
      
      // Фильтр по названию
      if(q) {
        ok = ok && title.includes(q);
      }
    }
    
    if(ok) visibleCount++;
    card.style.display=ok?'':'none';
  });
  
  // Обновляем счетчик найденных маршрутов
  const counter = document.querySelector('.routes-counter');
  if(counter) {
    counter.textContent = `Найдено: ${visibleCount} маршрутов`;
  }
}
document.querySelectorAll('.filter-radio').forEach(r=>r.addEventListener('change',applyFilters));
document.querySelectorAll('.days-input').forEach(i=>i.addEventListener('input',applyFilters));
if(filterSearch) filterSearch.addEventListener('input',applyFilters);

// Применяем фильтры при загрузке страницы
setTimeout(applyFilters, 100);

// Также применяем фильтры при загрузке DOM
document.addEventListener('DOMContentLoaded', applyFilters);

// Слайдер отзывов с кнопками
const reviewSlider=document.querySelector('.review-slider');
const btnNext=document.querySelector('.review-next');
const btnPrev=document.querySelector('.review-prev');
if(btnNext && btnPrev && reviewSlider){
  btnNext.addEventListener('click',()=>{reviewSlider.scrollBy({left:350,behavior:'smooth'});});
  btnPrev.addEventListener('click',()=>{reviewSlider.scrollBy({left:-350,behavior:'smooth'});});
}
});
